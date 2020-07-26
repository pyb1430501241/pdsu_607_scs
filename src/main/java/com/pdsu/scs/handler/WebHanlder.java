package com.pdsu.scs.handler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.pdsu.scs.bean.BlobInformation;
import com.pdsu.scs.bean.MyEmail;
import com.pdsu.scs.bean.MyImage;
import com.pdsu.scs.bean.MyLike;
import com.pdsu.scs.bean.Result;
import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.bean.WebInformation;
import com.pdsu.scs.exception.web.user.NotFoundUidAndLikeIdException;
import com.pdsu.scs.exception.web.user.NotFoundUidException;
import com.pdsu.scs.exception.web.user.UidAndLikeIdRepetitionException;
import com.pdsu.scs.exception.web.user.UidRepetitionException;
import com.pdsu.scs.service.MyEmailService;
import com.pdsu.scs.service.MyImageService;
import com.pdsu.scs.service.MyLikeService;
import com.pdsu.scs.service.UserInformationService;
import com.pdsu.scs.service.VisitInformationService;
import com.pdsu.scs.service.WebInformationService;
import com.pdsu.scs.utils.CodeUtils;
import com.pdsu.scs.utils.EmailUtils;
import com.pdsu.scs.utils.HashUtils;
import com.pdsu.scs.utils.RandomUtils;
import com.pdsu.scs.utils.ShiroUtils;
import com.pdsu.scs.utils.SimpleUtils;


/**
 * 
 * @author 半梦
 *
 */
@Controller
@RequestMapping("/user")
public class WebHanlder {
	
	private static final String EX = "exception";
	
	/**
	 * 用户信息相关
	 */
	@Autowired
	private UserInformationService userInformationService;
	
	/**
	 * 用户邮箱相关
	 */
	@Autowired
	private MyEmailService myEmailService;
	
	/**
	 * 关注相关
	 */
	@Autowired
	private MyLikeService myLikeService;
	
	/**
	 * 用户头像相关
	 */
	@Autowired
	private MyImageService myImageService;
	
	/**
	 * 用户博客相关
	 */
	@Autowired
	private WebInformationService webInformationService;
	
	/**
	 * 用户访问相关
	 */
	@Autowired
	private VisitInformationService visitInformationService;
	
	private static final String FILEPATH = "pdsu/web/img/";
	
	/**
	 * 日志
	 */
	private static final Logger log = LoggerFactory.getLogger(WebHanlder.class);
	
	/**
	 * 缓存管理器
	 */
	@Autowired
	private CacheManager cacheManager; 
	
	/**
	 * 缓存区, 用于存放验证码, 验证码有效期为五分钟
	 */
	private Cache cache = null;
	
	
	/**
	 * @return  用户的登录状态
	 * 如登录, 返回用户信息
	 * 反之返回提示语
	 */
	@RequestMapping(value = "/loginstatus", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public Result getLoginStatus() {
		try {
			UserInformation user = ShiroUtils.getUserInformation();
			if(user == null) {
				return Result.fail().add(EX, "未登录");
			}
			return Result.success().add("user", user);
		}catch (Exception e) {
			log.error("原因: " + e.getMessage());
			return Result.fail().add(EX, "未知错误, 请稍候重试");
		}
	}
	
	/**
	 * 处理登录
	 * @param uid 账号
	 * @param password 密码
	 * @param hit cache获取数据的key
	 * @param code 输入的验证码
	 * @param flag  是否记住密码 默认为不记住
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@CrossOrigin
	public Result login(String uid, String password, String hit, String code, 
			@RequestParam(value = "flag", defaultValue = "0")Integer flag) {
		log.info("账号: " + uid + "登录开始");
		log.info("参数为: " + SimpleUtils.toString(uid, password, hit, code, flag));
		if(cache.get(hit) == null) {
			return Result.fail().add(EX, "验证码已失效, 请刷新后重试");
		}
		//从缓存中获取验证码
		String ss = ((String) cache.get(hit).get()).toLowerCase();
		log.info("比对验证码中..." + " 用户输入验证码为: " + code + ", 服务器端储存的验证码为: " + ss);
		if(!ss.equals(code.toLowerCase())) {
			log.info("验证码错误");
			return Result.fail().add(EX, "验证码错误");
		}
		
		Subject subject = SecurityUtils.getSubject();
		/**
		 * 如果未认证
		 */
		if(ShiroUtils.getUserInformation() == null) {
			log.info("账号: " + uid + "开始登录认证");
			UsernamePasswordToken token = new UsernamePasswordToken(uid+"", password);
			//是否记住
			if(flag == 0) {
				token.setRememberMe(false);
			}else {
				token.setRememberMe(true);
			}
			try{
				//登录
				subject.login(token);
				UserInformation uu = (UserInformation) subject.getPrincipal();
				uu.setPassword(null);
				if(uu.getAccountStatus() == 2) {
					log.info("账号: " + uid + "登录失败, 原因: 账号被冻结");
					return Result.fail().add(EX, "账号被冻结");
				}
				if(uu.getAccountStatus() == 3) {
					log.info("账号: " + uid + "登录失败, 原因: 账号被封禁");
					return Result.fail().add(EX, "账号被封禁");
				}
				if(uu.getAccountStatus() == 4) {
					log.info("账号: " + uid + "登录失败, 原因: 账号已被注销");
					return Result.fail().add(EX, "账号已被注销");
				}
				log.info("账号: " + uid + "登录成功, sessionid为: " + subject.getSession().getId());
				uu.setImgpath(myImageService.selectImagePathByUid(uu.getUid()).getImagePath());
				return Result.success()
						.add("user", uu)
						.add("AccessToken", subject.getSession().getId());
			}catch (IncorrectCredentialsException e) {
				log.info("账号: " + uid + "密码错误");
				return Result.fail().add(EX, "账号或密码错误");
			}catch (UnknownAccountException e) {
				log.info("账号: " + uid + "不存在");
				return Result.fail().add(EX, "账号不存在");
			}catch (Exception e) {
				log.error("账号: " + uid + "登录时发生未知错误, 原因: " + e.getMessage());
				return Result.fail().add(EX, "未知错误");
			}
		}
		log.info("账号: " + uid + "已登录");
		return Result.fail().add(EX, "你已登录");
	}
	
	/**
	 * 获取验证码
	 * 储存验证码到缓存区 cache
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getcodeforlogin", method = RequestMethod.GET)
	@CrossOrigin
	public Result getCode(HttpServletRequest request){
		try {
			log.info("获取验证码开始");
			if(cache == null) {
				System.setProperty("java.awt.headless", "true");
				cache = cacheManager.getCache("code");
			}
			String verifyCode = CodeUtils.generateVerifyCode(4);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			CodeUtils.outputImage(100, 30, out, verifyCode);
			String base64 = Base64.encodeToString(out.toByteArray());
			String src = "data:image/png;base64," + base64;
			String token = RandomUtils.getUUID();
			cache.put(token, verifyCode);
			log.info("获取成功, 验证码为: " + verifyCode);
			return Result.success()
					.add("token", token)
					.add("img", src)
					.add("code", verifyCode);
		} catch (Exception e) {
			if(e instanceof InvocationTargetException) {
				log.error(((InvocationTargetException)e).getTargetException().getMessage());
			}else {
				log.info("获取验证码失败, 原因: " + e.getMessage());
			}
			return Result.fail().add(EX, "获取验证码失败, 未知原因");
		}
	}
	
	/**
	 * 申请账号时发送邮箱验证码
	 * @param email 前端输入邮箱
	 * @param name  前端输入网名
	 * @return
	 */
	@RequestMapping(value = "/getcodeforapply", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public Result sendEmailforApply(@RequestParam("email")String email, @RequestParam("name")String name) {
		try {
			log.info("邮箱: " + email + "开始申请账号, 发送验证码");
			if(email == null) {
				return Result.fail().add(EX, "邮箱不可为空");
			}
			if(name == null) {
				return Result.fail().add(EX, "用户名不可为空");
			}
			if(myEmailService.countByEmail(email)) {
				return Result.fail().add(EX, "此邮箱已被绑定");
			}
			EmailUtils utils = new EmailUtils();
			utils.sendEmailForApply(email, name);
			String text = utils.getText();
			if(cache == null) {
				cache = cacheManager.getCache("code");
			}
			String token = RandomUtils.getUUID();
			cache.put(token, text);
			log.info("发送成功, 验证码为: " + text);
			return Result.success().add("token", token);
		}catch (Exception e) {
			log.error("邮箱: " + email + " 不存在, 原因: " + e.getMessage());
			return Result.fail().add(EX, "邮箱地址不正确");
		}
	}
	
	/**
	 * 申请账号
	 * @param user  POJO 类
	 * @param email 邮箱
	 * @param token 获取验证码的 key
	 * @param code  前端输入验证码
	 * @return json字符串
	 */
	@RequestMapping(value = "/applynumber", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result applyforAccountNumber(UserInformation user, String email, String token, String code) {
		try {
			log.info("申请账号: " + user.getUid() + "开始");
			//验证验证码
			if(cache.get(token) == null) {
				return Result.fail().add(EX, "验证码已过期, 请重新获取");
			}
			String ss = (String) cache.get(token).get();
			if(!ss.equals(code)) {
				return Result.fail().add(EX, "验证码错误");
			}
			//查询账号是否已存在
			if(userInformationService.countByUid(user.getUid()) != 0) {
				return Result.fail().add(EX, "该账号已存在,是否忘记密码?");
			}
			if(myEmailService.countByEmail(email)) {
				return Result.fail().add(EX, "此邮箱已被绑定, 忘记密码?");
			}
			if(userInformationService.countByUserName(user.getUsername()) != 0) {
				return Result.fail().add(EX, "用户名已存在");
			}
			//默认账号为正常状态
			user.setAccountStatus(1);
			//设置申请时间
			user.setTime(SimpleUtils.getSimpleDate());
			//设置默认头像
			user.setImgpath("01.png");
			boolean flag = userInformationService.inset(user);
			if(flag) {
				myImageService.insert(new MyImage(user.getUid(), "01.png"));
				myEmailService.insert(new MyEmail(null, user.getUid(), email));
				log.info("申请账号: " + user.getUid() + "成功, " + "账号信息为:" + user);
				return Result.success();
			}else {
				log.error("申请账号: " + user.getUid() + "失败, 此账号已存在");
				return Result.fail().add(EX, "申请失败");
			}
		} catch (UidRepetitionException e) {
			log.info("该用户已存在, 原因: " + e.getMessage());
			return Result.fail().add(EX, e.getMessage());
		} catch (Exception e) {
			log.error("申请账号失败, 原因: " + e.getMessage());
			return Result.fail().add(EX, "连接服务器失败, 请稍候重试");
		}
	}
	
	/**
	 * 找回密码API集开始
	 *	===============================================================================================
	 */
	
	/**
	 *  该API为用户输入账号验证此用户是否存在
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "/isexist", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public Result getEmail(Integer uid) {
		try {
			log.info("开始进行找回密码验证账号是否存在");
			int i = userInformationService.countByUid(uid);
			if(i != 0) {
				log.info("账号: " + uid + "存在");
				MyEmail email = myEmailService.selectMyEmailByUid(uid);
				if(cache == null) {
					cache = cacheManager.getCache("code");
				}
				String token = RandomUtils.getUUID();
				cache.put(token, email.getEmail());
				email.setEmail(SimpleUtils.getAsteriskForString(email.getEmail()));
				return Result.success().add("email", email)
						.add("uid", uid)
						.add("token", token);
			}else {
				log.info("账号: " + uid + "不存在");
				return Result.fail().add(EX, "账号不存在, 是否申请?");
			}
		}catch (Exception e) {
			log.error("账号: " + uid + "找回密码时服务器开小差了, 原因: " + e.getMessage());
			return Result.fail().add(EX, "未定义类型错误");
		}
	}
	
	/**
	 * 找回密码时发送验证码
	 * @param email  前端传入
	 * @return
	 */
	@RequestMapping(value = "/getcodeforretrieve", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public Result sendEmailForRetrieve(String token) {
		String email = null;
		try {
			if(cache.get(token) == null) {
				return Result.fail().add(EX, "验证信息已失效, 请重新验证");
			}
			email = (String) cache.get(token).get();
			log.info("邮箱: " + email + " 开始发送找回密码的验证码");
			EmailUtils utils = new EmailUtils();
			//发送邮件
			utils.sendEmailForRetrieve(email, 
					userInformationService.selectByUid(
							myEmailService.selectMyEmailByEmail(email).getUid()).getUsername());
			String text = utils.getText();
			String uuid = RandomUtils.getUUID();
			cache.put(uuid, text);
			log.info("发送验证码成功, 验证码为: " + text);
			return Result.success().add("token", uuid);
		}catch (Exception e) {
			log.error("邮箱: " + email + "找回密码时发送邮件时服务器开小差了, 原因: " + e.getMessage());
			return Result.fail().add(EX, "未定义类型错误");
		}
	}
	
	/**
	 * 找回密码
	 * @param uid
	 * @param token
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/retrieve", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result retrieveThePassword(Integer uid, String password,String token, String code) {
		try {
			log.info("账号: " + uid + "开始找回密码");
			if(cache.get(token) == null) {
				return Result.fail().add(EX, "验证码已过期");
			}
			String ss = (String) cache.get(token).get();
			if(!ss.equals(code)) {
				return Result.fail().add(EX, "验证码错误"); 
			}
			boolean b = userInformationService.modifyThePassword(uid, password);
			if(!b) {
				return Result.fail().add(EX, "密码找回失败, 请稍后重试");
			}
			log.info("账号: " + uid + "找回成功, 新密码为: " + HashUtils.getPasswordHash(uid, password));
			return Result.success().add(EX, "找回成功");
		}catch (Exception e) {
			log.error("账号: " + uid + "找回失败, 失败原因: " + e.getMessage());
			return Result.fail().add(EX, "未知错误");
		}
	}
	
	/**
	 * 找回密码 api 结束
	 * ===============================================================================================
	 */
	
	
	
	/**
	 * 修改密码 API 集
	 * ===============================================================================================
	 */
	/**
	 * 获取修改密码页面数据
	 * @return
	 */
	@RequestMapping(value = "/getmodify", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public Result getModify() {
		try {
			log.info("修改密码开始, 用户信息从 session 获取");
			UserInformation user = ShiroUtils.getUserInformation();
			MyEmail myEmail = myEmailService.selectMyEmailByUid(user.getUid());
			log.info("信息获取成功");
			return Result.success().add("myEmail", myEmail);
		}catch (Exception e) {
			log.info("修改密码失败, 原因: " + e.getMessage());
			return Result.fail();
		}
	}
	
	/**
	 * 获取验证码
	 * @param email 前端获取 
	 * @return
	 */
	@RequestMapping(value = "/getcodeformodify", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public Result sendEmailForModify(String email) {
		try {
			log.info("邮箱: " + email + "开始发送修改密码的验证码");
			EmailUtils utils = new EmailUtils();
			utils.sendEmailForModify(email, ShiroUtils.getUserInformation().getUsername());
			if(cache == null) {
				cache = cacheManager.getCache("code");
			}
			String token = RandomUtils.getUUID();
			String text = utils.getText();
			cache.put(token, text);
			log.info("邮箱: " + email + "发送验证码成功, 验证码为: " + text);
			return Result.success().add("token", token);
		}catch (Exception e) {
			log.info("邮箱: " + email + "发送修改密码的验证码失败, 错误信息为: " + e.getMessage());
			return Result.fail().add(EX, "未知错误");
		}
	}
	
	/**
	 * 验证码验证
	 * @param token 取出验证码的 key 前端获取
	 * @param code  前端输入验证码
	 * @return
	 */
	@RequestMapping(value = "/modifybefore", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public Result modifyBefore(String token, String code) {
		try {
			log.info("开始验证验证码是否正确");
			if(cache.get(token) == null) {
				log.info("验证码已过期, key 为: " + token);
				return Result.fail().add(EX, "验证码已过期");
			}
			if(!cache.get(token).get().equals(code)) {
				log.info("验证码错误, 服务器端验证码为: " + cache.get(token).get() + ", 用户输入为: " + code);
				return Result.fail().add(EX, "验证码错误");
			}
			log.info("验证码: " + code + "正确");
			return Result.success();
		}catch (Exception e) {
			log.error("验证验证码时发生未知错误");
			return Result.fail().add(EX, "未知错误");
		}
	}
	
	/**
	 * 修改密码
	 * @param password 新密码
	 * @return
	 */
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	@CrossOrigin
	@ResponseBody
	public Result modifyForPassword(String password) {
		Integer uid = null;
		try {
			uid = ShiroUtils.getUserInformation().getUid();
			log.info("账号: " + uid + "开始修改密码");
			boolean flag = userInformationService.modifyThePassword(
					ShiroUtils.getUserInformation().getUid(), password);
			if(!flag) {
				log.info("账号: " + uid + " 修改密码失败");
				return Result.fail().add(EX, "修改失败");
			}
			log.info("账号: " + uid + "修改密码成功, 新密码为: " + HashUtils.getPasswordHash(uid, password));
			return Result.success();
		}catch (Exception e) {
			if(uid == null) {
				log.info("修改密码失败, 用户未登录");
				return Result.fail().add(EX, "用户未登录");
			}
			log.error("账号: " + uid + "修改密码时发生错误");
			return Result.fail().add(EX, "未知错误");
		}
	}
	
	/**
	 * 修改密码 api 集结束
	 * ===============================================================================================
	 */
	
	/**
	 * 处理关注请求, 作者的学号由前端获取, 关注人学号从session里获取
	 * 
	 * @param uid  作者的学号
	 * @return
	 */
	@RequestMapping(value = "/like", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result like(Integer uid) {
		Integer likeId = null;
		try {
			likeId = ShiroUtils.getUserInformation().getUid();
			log.info("用户: " + likeId + ", 关注: " + uid + "开始");
			//插入记录
			boolean flag = myLikeService.insert(new MyLike(null, likeId, uid));
			if(flag) {
				log.info("用户: " + likeId + ", 关注: " + uid + "成功");
				return Result.success();
			}
			log.info("用户: " + likeId + ", 关注: " + uid + "失败, 原因: 数据库连接失败");
			return Result.fail().add(EX, "网络连接失败, 请稍候重试");
		} catch (UidAndLikeIdRepetitionException e) {
			log.info("用户: " + likeId + ", 关注: " + uid + "失败, 原因: " + e.getMessage());
			return Result.fail().add(EX, e.getMessage());
		} catch (Exception e) {
			if(likeId == null) {
				log.info("关注失败, 用户未登录");
				return Result.fail().add(EX, "用户未登录");
			}
			log.error("用户: " + likeId + ", 关注: " + uid + "失败" + ", 原因为: " + e.getMessage());
			return Result.fail().add(EX, "未定义类型错误");
		}
	}
	
	/**
	 * 处理取消关注请求, 作者的学号由前端获取, 关注人学号从session里获取
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = "/delike", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result delike(Integer uid) {
		Integer likeId = null;
		try {
			likeId = ShiroUtils.getUserInformation().getUid();
			log.info("用户: " + likeId + ", 取消关注: " + uid + "开始");
			//刪除记录
			boolean b = myLikeService.deleteByLikeIdAndUid(likeId, uid);
			if(b) {
				log.info("用户: " + likeId + ", 取消关注: " + uid + "成功");
				return Result.success();
			}
			log.info("用户: " + likeId + ", 取消关注: " + uid + "失败, 原因: 数据库连接失败");
			return Result.fail().add(EX, "网络连接失败, 请稍候重试");
		} catch (NotFoundUidAndLikeIdException e) {
			log.info("用户: " + likeId + ", 取消关注: " + uid + "失败, 原因: " + e.getMessage());
			return Result.fail().add(EX, e.getMessage());
		} catch (Exception e) {
			log.info("用户: " + likeId + ", 取消关注: " + uid + "失败, 原因: " + e.getMessage());
			if(likeId == null) {
				return Result.fail().add(EX, "用户未登录");
			}
			return Result.fail().add(EX, "未定义类型错误");
		}
	}
	
	/**
	 * 静态代码块
	 * 检测储存头像的文件夹是否建立
	 * 如未创建, 则创建
	 */
	static {
		File file = new File(FILEPATH);
		if(file.exists()) {
			file.mkdirs();
		}
	}
	
	/**
	 * 更换头像
	 * @param img  上传头像文件
	 * @return
	 */
	@RequestMapping(value = "/updateimage", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result updateImage(@RequestParam("img")MultipartFile img) {
		UserInformation user = null;
		try{
			user = ShiroUtils.getUserInformation();
			log.info("用户: " + user.getUid() + " 更换头像开始");
			String name = HashUtils.getFileNameForHash(user.getUid()+"") + SimpleUtils.getSuffixName(img.getOriginalFilename());
			new Thread(()->{
				byte [] by = null;
				try {
					by = FileUtils.readFileToByteArray(new File(FILEPATH + name));
				} catch (IOException e1) {
					log.info("原头像为默认头像");
				}
				try {
					log.info("写入新头像");
					FileUtils.writeByteArrayToFile(new File(FILEPATH + name), img.getBytes(), false);
				} catch (IOException e) {
					try {
						log.info("写入新头像失败, 还原为原头像");
						FileUtils.writeByteArrayToFile(new File(FILEPATH + name), by, false);
					} catch (IOException e1) {
						log.error("写入头像失败, 原因: " + e1.getMessage());
					}
				}
			}).start();
			log.info("用户: " + user.getUid() + " 更换头像成功, 开始写入数据库地址");
			boolean b = myImageService.update(new MyImage(user.getUid(), name));
			if(b) {
				log.info("用户: " + user.getUid() + " 更换头像成功");
				return Result.success().add("imgpath", name);
			} else {
				log.warn("写入数据库失败!");
				return Result.fail().add(EX, "网络异常, 请稍后重试");
			}
		}catch (Exception e) {
			log.error("用户更换头像失败, 原因: " + e.getMessage());
			if(user == null) {
				return Result.fail().add(EX, "用户未登录");
			}
			return Result.fail().add(EX, "未定义类型错误"); 
		}		
	}
	
	/**
	 * 更换用户名
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/updatename", method = RequestMethod.POST)
	public Result updateUserName(String username) {
		UserInformation user = null;
		try {
			user = ShiroUtils.getUserInformation();
			log.info("用户: " + user.getUid() + "更换用户名开始, 原用户名: " + user.getUsername());
			boolean b = userInformationService.updateUserName(user.getUid(), username);
			if(b) {
				log.info("用户: " + user.getUid() + "更换用户名成功, 现用户名: " + username);
				return Result.success();
			}else {
				log.warn("连接数据库失败");
				return Result.fail().add(EX, "网络异常, 请稍候重试");
			}
		} catch (NotFoundUidException e) {
			log.info(e.getMessage());
			return Result.fail().add(EX, e.getMessage());
		}catch (Exception e) {
			if(user == null) {
				log.info("用户未登录");
				return Result.fail().add(EX, "用户未登录");
			}else {
				log.error("用户: " + user.getUid() + "更换用户名失败, 原因: " + e.getMessage());
			}
			return Result.fail().add(EX, "未定义类型错误");
		}
	}
	
	@RequestMapping(value = "/getblobs", method = RequestMethod.GET)
	@CrossOrigin
	@ResponseBody
	public Result getBlobsByUid() {
		UserInformation user = null;
		try {
			user = ShiroUtils.getUserInformation();
			log.info("用户: " + user.getUid() + "获取自己的文章开始");
			List<WebInformation> weblist = webInformationService.selectWebInformationsByUid(user.getUid());
			List<Integer> webids = new ArrayList<Integer>();
			for (WebInformation web : weblist) {
				webids.add(web.getId());
			}
			List<Integer> visitList = visitInformationService.selectVisitsByWebIds(webids);
			List<BlobInformation> blobList = new ArrayList<BlobInformation>();
			for(int i = 0; i < weblist.size(); i++) {
				blobList.add(new BlobInformation(
						user, weblist.get(i), visitList.get(i), null, null
				));
			}
			return Result.success().add("blobList", blobList);
		} catch (NotFoundUidException e) {
			log.info("用户获取自己文章失败, 原因: " + e.getMessage());
			return Result.fail().add(EX, e.getMessage());
		}catch (Exception e) {
			if(user == null) {
				log.info("获取文章失败, 用户未登录");
				return Result.fail().add(EX, "未登录");
			}
			log.error("用户获取自己文章失败, 原因:  " + e.getMessage());
			return Result.fail().add(EX, "未定义类型错误");
		}
	}
}

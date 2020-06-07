package com.pdsu.scs.handler;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pdsu.scs.bean.MyEmail;
import com.pdsu.scs.bean.MyLike;
import com.pdsu.scs.bean.Result;
import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.service.MyEmailService;
import com.pdsu.scs.service.MyLikeService;
import com.pdsu.scs.service.UserInformationService;
import com.pdsu.scs.utils.CodeUtils;
import com.pdsu.scs.utils.EmailUtils;
import com.pdsu.scs.utils.HashUtils;
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
	
	@Autowired
	private UserInformationService userInformationService;
	
	@Autowired
	private MyEmailService myEmailService;
	
	@Autowired
	private MyLikeService myLikeService;
	
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
	
	@RequestMapping("/login")
	public String login() {
		return "login";
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
	@RequestMapping("/loginAjax")
	@CrossOrigin
	public Result loginAjax(String uid, String password, String hit, String code, 
			@RequestParam(value = "flag", defaultValue = "0")Integer flag) {
		log.info("账号: " + uid + "登录开始");
		Subject subject = SecurityUtils.getSubject();
		if(cache.get(hit) == null) {
			return Result.fail().add(EX, "验证码已失效, 请刷新后重试");
		}
		//从缓存中获取验证码
		String ss = (String) cache.get(hit).get();
		log.info("比对验证码中..." + " 用户输入验证码为: " + code + ", 服务器端储存的验证码为: " + ss);
		if(!ss.equals(code)) {
			log.info("验证码错误");
			return Result.fail().add(EX, "验证码错误");
		}
		/**
		 * 如果未认证
		 */
		if(!subject.isAuthenticated()) {
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
				log.info("账号: " + uid + "登录成功");
				return Result.success().add(EX, "登录成功")
						.add("sessionId", subject.getSession().getId())
						.add("user", uu);
			}catch (IncorrectCredentialsException e) {
				log.info("账号: " + uid + "密码错误");
				return Result.fail().add(EX, "账号或密码错误");
			}catch (UnknownAccountException e) {
				log.info("账号: " + uid + "不存在");
				return Result.fail().add(EX, "账号不存在");
			}catch (Exception e) {
				log.error("账号: " + uid + "登录时发生未知错误");
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
	@RequestMapping("/getCodeForLogin")
	@CrossOrigin
	public Result getCode(){
		try {
			log.info("获取验证码开始");
			if(cache == null) {
				cache = cacheManager.getCache("code");
			}
			CodeUtils utils = new CodeUtils();
			BufferedImage image = utils.getImage();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			CodeUtils.output(image, out);
			String base64 = Base64.encodeToString(out.toByteArray());
			String src = "data:image/png;base64," + base64;
			String token = UUID.randomUUID().toString();
			cache.put(token, utils.getText());
			log.info("获取成功, 验证码为: " + utils.getText());
			return Result.success().add("img", src).add("token", token);
		} catch (IOException e) {
			log.error("获取验证码失败! 错误信息: " + e.getMessage());
			return Result.fail();
		}
	}
	
	/**
	 * 申请账号时发送邮箱验证码
	 * @param email 前端输入邮箱
	 * @param name  前端输入网名
	 * @return
	 */
	@RequestMapping("/getCodeForApply")
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
			if(myEmailService.countByEmail(email) != 0) {
				return Result.fail().add(EX, "此邮箱已被绑定");
			}
			EmailUtils utils = new EmailUtils();
			utils.sendEmailForApply(email, name);
			String text = utils.getText();
			if(cache == null) {
				cache = cacheManager.getCache("code");
			}
			String token = UUID.randomUUID().toString();
			cache.put(token, text);
			log.info("发送成功, 验证码为: " + text);
			return Result.success().add("token", token);
		}catch (Exception e) {
			log.error("邮箱: " + email + " 不存在");
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
	@RequestMapping("/applyNumber")
	@ResponseBody
	@CrossOrigin
	public Result applyforAccountNumber(UserInformation user, String email, String token, String code) {
		try {
			log.info("申请账号: " + user.getUid() + "开始");
			//验证验证码
			if(cache.get(token) == null) {
				return Result.fail().add(EX, "验证码已过期");
			}
			String ss = (String) cache.get(token).get();
			if(!ss.equals(code)) {
				return Result.fail().add(EX, "验证码错误");
			}
			//查询账号是否已存在
			if(userInformationService.countByUid(user.getUid()) != 0) {
				return Result.fail().add(EX, "该账号已存在,是否忘记密码?");
			}
			if(myEmailService.countByEmail(email) != 0) {
				return Result.fail().add(EX, "此邮箱已被绑定, 忘记密码?");
			}
			//默认账号为正常状态
			user.setAccountStatus(1);
			//设置申请时间
			user.setTime(SimpleUtils.getSimpleDate());
			//设置默认头像
			user.setImgpath("01.png");
			boolean flag = userInformationService.inset(user);
			if(flag) {
				log.info("申请账号: " + user.getUid() + "成功, " + "账号信息为:" +user);
				return Result.success().add(EX, "申请成功");
			}else {
				log.error("申请账号: " + user.getUid() + "失败, 此账号已存在");
				return Result.fail().add(EX, "申请失败");
			}
		}catch (Exception e) {
			log.error("申请账号: " + user.getUid() + "失败, 服务器开小差了");
			return Result.fail().add(EX, "连接服务器失败, 请稍候重试");
		}
	}
	
	/**
	 * 找回密码API集
	 */
	/**
	 *  该API为用户输入账号验证此用户是否存在
	 * @param email
	 * @return
	 */
	@RequestMapping("/inputUid")
	@ResponseBody
	@CrossOrigin
	public Result inputEmail(Integer uid) {
		try {
			log.info("开始进行找回密码验证账号是否存在");
			int i = userInformationService.countByUid(uid);
			if(i != 0) {
				log.info("账号: " + uid + "存在");
				return Result.success().add("email", myEmailService.selectMyEmailByUid(uid))
						.add("uid", uid);
			}else {
				log.info("账号: " + uid + "不存在");
				return Result.fail();
			}
		}catch (Exception e) {
			log.error("账号: " + uid + "找回密码时服务器开小差了");
			return Result.fail();
		}
	}
	
	/**
	 * 找回密码时发送验证码
	 * @param email  前端传入
	 * @return
	 */
	@RequestMapping("/getCodeForRetrieve")
	@ResponseBody
	@CrossOrigin
	public Result sendEmailForRetrieve(String email) {
		try {
			log.info("邮箱: " + email + " 开始发送找回密码的验证码");
			EmailUtils utils = new EmailUtils();
			//发送邮件
			utils.sendEmailForRetrieve(email, 
					userInformationService.selectByUid(
							myEmailService.selectMyEmailByEmail(email).getUid()).getUsername());
			String text = utils.getText();
			String token = UUID.randomUUID().toString();
			if(cache == null) {
				cache = cacheManager.getCache("code");
			}
			cache.put(token, text);
			log.info("发送验证码成功, 验证码为: " + text);
			return Result.success().add("token", token);
		}catch (Exception e) {
			log.error("邮箱: " + email + "找回密码时发送邮件时服务器开小差了");
			return Result.fail();
		}
	}
	/**
	 * 
	 * @param uid
	 * @param token
	 * @param code
	 * @return
	 */
	@RequestMapping("/retrieve")
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
			boolean b = userInformationService.ModifyThePassword(uid, password);
			if(!b) {
				return Result.fail().add(EX, "密码修改失败, 请稍后重试");
			}
			log.info("账号: " + uid + "找回成功, 新密码为: " + HashUtils.getPasswordHash(uid, password));
			return Result.success().add(EX, "修改成功");
		}catch (Exception e) {
			log.error("账号: " + uid + "找回失败, 失败原因未知");
			return Result.fail().add(EX, "未知错误");
		}
	}
	
	/**
	 * 修改密码 API集
	 */
	/**
	 * 获取修改密码页面数据
	 * @return
	 */
	@RequestMapping("/getModify")
	@ResponseBody
	@CrossOrigin
	public Result getModify() {
		try {
			log.info("修改密码开始, 用户信息从 session获取");
			UserInformation user = ShiroUtils.getUserInformation();
			MyEmail myEmail = myEmailService.selectMyEmailByUid(user.getUid());
			log.info("信息获取成功");
			return Result.success().add("myEmail", myEmail);
		}catch (Exception e) {
			return Result.fail();
		}
	}
	
	/**
	 * 获取验证码
	 * @param email 前端获取 
	 * @return
	 */
	@RequestMapping("/getCodeForModify")
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
			String token = UUID.randomUUID().toString();
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
	@RequestMapping("/modifyBefore")
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
	@RequestMapping("/modify")
	@CrossOrigin
	@ResponseBody
	public Result modifyForPassword(String password) {
		Integer uid = ShiroUtils.getUserInformation().getUid();
		try {
			log.info("账号: " + uid + "开始修改密码");
			boolean flag = userInformationService.ModifyThePassword(
					ShiroUtils.getUserInformation().getUid(), password);
			if(!flag) {
				log.info("账号: " + uid + " 修改密码失败");
				return Result.fail().add(EX, "修改失败");
			}
			log.info("账号: " + uid + "修改密码成功, 新密码为: " + HashUtils.getPasswordHash(uid, password));
			return Result.success();
		}catch (Exception e) {
			log.error("账号: " + uid + "修改密码时发生错误");
			return Result.fail().add(EX, "未知错误");
		}
	}
	
	/**
	 * 处理关注请求, 作者的学号由前端获取, 关注人学号从session里获取
	 * 
	 * @param uid  作者的学号
	 * @return
	 */
	@RequestMapping("/like")
	@ResponseBody
	@CrossOrigin
	public Result like(Integer uid) {
		//从session里获取当前登录的人的学号
		Integer likeId = ShiroUtils.getUserInformation().getUid();
		try {
			log.info("用户: " + likeId + ", 关注: " + uid + "开始");
			//插入记录
			boolean flag = myLikeService.insert(new MyLike(null, likeId, uid));
			if(flag) {
				log.info("用户: " + likeId + ", 关注: " + uid + "成功");
				return Result.success();
			}
			log.info("用户: " + likeId + ", 关注: " + uid + "失败");
			return Result.fail();
		}catch (Exception e) {
			log.error("用户: " + likeId + ", 关注: " + uid + "失败" + ", 原因为: " + e.getMessage());
			return Result.fail();
		}
	}
}

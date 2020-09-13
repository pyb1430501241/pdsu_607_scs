/*
“最最喜欢你，绿子。”
“什么程度？”
“像喜欢春天的熊一样。”
“春天的熊？”绿子再次扬起脸，“什么春天的熊？”
“春天的原野里，你一个人正走着，对面走来一只可爱的小熊，浑身的毛活像天鹅绒，眼睛圆鼓鼓的。
 它这么对你说到：‘你好，小姐，和我一块打滚玩好么？’接着，你就和小熊抱在一起，顺着长满三叶
 草的山坡咕噜咕噜滚下去，整整玩了一大天。你说棒不棒？”
“太棒了。”
“我就这么喜欢你。”
 */
package com.pdsu.scs.handler;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pdsu.scs.bean.*;
import com.pdsu.scs.service.*;
import com.pdsu.scs.utils.*;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * 
 * @author 半梦
 *
 */
@Controller
@RequestMapping("/user")
public class UserHandler extends ParentHandler{

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

	/**
	 * 浏览记录
	 */
	@Autowired
	private UserBrowsingRecordService userBrowsingRecordService;

	/**
	 * 文件
	 */
	@Autowired
	private WebFileService webFileService;

	/**
	 * 通知
	 */
	@Autowired
	private SystemNotificationService systemNotificationService;

	/**
	 * 管理
	 */
	@Autowired
	private UserRoleService userRoleService;
	
	/**
	 * 日志
	 */
	private static final Logger log = LoggerFactory.getLogger(UserHandler.class);
	
	/**
	 * 缓存管理器
	 */
	@Autowired
	private CacheManager cacheManager; 
	
	/**
	/**
	 * 缓存区
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
	public Result getLoginStatus() throws Exception{
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		user.setSystemNotifications(systemNotificationService.countSystemNotificationByUidAndUnRead(user.getUid()));
		return Result.success().add("user", user);

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
	public Result login(@RequestParam String uid, @RequestParam String password,
						@RequestParam String hit, @RequestParam String code,
						@RequestParam(value = "flag", defaultValue = "0")Integer flag) throws Exception {
		log.info("账号: " + uid + "登录开始");
		log.info("参数为: " + SimpleUtils.toString(uid, password, hit, code, flag));
		if(Objects.isNull(cache.get(hit))) {
			return Result.fail().add(EXCEPTION, CODE_EXPIRED);
		}
		//从缓存中获取验证码
		String ss = ((String) cache.get(hit).get()).toLowerCase();
		log.info("比对验证码中..." + " 用户输入验证码为: " + code + ", 服务器端储存的验证码为: " + ss);
		if(!ss.equals(code.toLowerCase())) {
			log.info(CODE_ERROR);
			return Result.fail().add(EXCEPTION, CODE_ERROR);
		}
		Subject subject = SecurityUtils.getSubject();
		if(!Objects.isNull(ShiroUtils.getUserInformation())) {
		    subject.logout();
        }
        log.info("账号: " + uid + "开始登录认证");
        UsernamePasswordToken token = new UsernamePasswordToken(uid+"", password);
        //是否记住
        if(flag == 0) {
            token.setRememberMe(false);
        } else {
            token.setRememberMe(true);
        }
        subject.login(token);
        UserInformation uu = (UserInformation) subject.getPrincipal();
        uu.setPassword(null);
        log.info("账号: " + uid + "登录成功, sessionid为: " + subject.getSession().getId());
        uu.setImgpath(myImageService.selectImagePathByUid(uu.getUid()).getImagePath());
        uu.setSystemNotifications(systemNotificationService.countSystemNotificationByUidAndUnRead(uu.getUid()));
        uu.setEmail(SimpleUtils.getAsteriskForString(myEmailService.selectMyEmailByUid(uu.getUid()).getEmail()));
        return Result.success()
					.add("user", uu)
					.add("AccessToken", subject.getSession().getId());
	}
	
	/**
	 * 获取验证码
	 * 储存验证码到缓存区 cache
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getcodeforlogin", method = RequestMethod.GET)
	@CrossOrigin
	public Result getCode() throws Exception {
		log.info("获取验证码开始");
		if(Objects.isNull(cache)) {
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
				.add("vicode", verifyCode);
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
	public Result sendEmailforApply(@RequestParam("email")String email, @RequestParam("name")String name) throws Exception{
			log.info("邮箱: " + email + "开始申请账号, 发送验证码");
			if(StringUtils.isEmpty(email)) {
				return Result.fail().add(EXCEPTION, "邮箱不可为空");
			}
			if(StringUtils.isEmpty(name)) {
				return Result.fail().add(EXCEPTION, "用户名不可为空");
			}
			if(myEmailService.countByEmail(email)) {
				return Result.fail().add(EXCEPTION, "此邮箱已被绑定");
			}
			EmailUtils utils = new EmailUtils();
			utils.sendEmailForApply(email, name);
			String text = utils.getText();
			if(Objects.isNull(cache)) {
				cache = cacheManager.getCache("code");
			}
			String token = RandomUtils.getUUID();
			cache.put(token, text);
			log.info("发送成功, 验证码为: " + text);
			return Result.success().add("token", token);
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
	public Result applyforAccountNumber(@Valid UserInformation user, @RequestParam String email,
										@RequestParam String token, @RequestParam String code)
			throws Exception {
		log.info("申请账号: " + user.getUid() + "开始");
		//验证验证码
		Cache.ValueWrapper valueWrapper = cache.get(token);
		if(Objects.isNull(valueWrapper)) {
			return Result.fail().add(EXCEPTION, CODE_EXPIRED);
		}
		String ss = (String) valueWrapper.get();
		if(!ss.equals(code)) {
			return Result.fail().add(EXCEPTION, CODE_ERROR);
		}
		if(userInformationService.countByUid(user.getUid()) != 0) {
			return Result.fail().add(EXCEPTION, "该账号已存在,是否忘记密码?");
		}
		if(myEmailService.countByEmail(email)) {
			return Result.fail().add(EXCEPTION, "此邮箱已被绑定, 忘记密码?");
		}
		if(userInformationService.countByUserName(user.getUsername()) != 0) {
			return Result.fail().add(EXCEPTION, "用户名已存在");
		}
		user.setAccountStatus(1);
		user.setTime(SimpleUtils.getSimpleDate());
		user.setImgpath(Default_User_Img_Name);
		boolean flag = userInformationService.inset(user);
		if(flag) {
			myImageService.insert(new MyImage(user.getUid(), Default_User_Img_Name));
			myEmailService.insert(new MyEmail(null, user.getUid(), email));
			userRoleService.insert(new UserRole(user.getUid(), 1));
			log.info("申请账号: " + user.getUid() + "成功, " + "账号信息为:" + user);
			return Result.success();
		}
		log.error("申请账号: " + user.getUid() + "失败, 此账号已存在");
		return Result.fail().add(EXCEPTION, "申请失败");
	}
	
	/**
	 * 找回密码API集开始
	 *	===============================================================================================
	 */
	
	/**
	 *  该API为用户输入账号验证此用户是否存在
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = "/isexist", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public Result getEmail(@RequestParam Integer uid) throws Exception{
		log.info("开始进行找回密码验证账号是否存在");
		int i = userInformationService.countByUid(uid);
		if(i != 0) {
			log.info("账号: " + uid + "存在");
			MyEmail email = myEmailService.selectMyEmailByUid(uid);
			if(Objects.isNull(cache)) {
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
			return Result.fail().add(EXCEPTION, "账号不存在, 是否申请?");
		}
	}
	
	/**
	 * 找回密码时发送验证码
	 * @param token  前端传入
	 * @return
	 */
	@RequestMapping(value = "/getcodeforretrieve", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public Result sendEmailForRetrieve(@RequestParam String token) throws Exception{
		String email = null;
		Cache.ValueWrapper valueWrapper = cache.get(token);
		if(Objects.isNull(valueWrapper)) {
			return Result.fail().add(EXCEPTION, "验证信息已失效, 请重新验证");
		}
		email = (String) valueWrapper.get();
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
	public Result retrieveThePassword(@RequestParam Integer uid,
									  @RequestParam String password,
									  @RequestParam String token,
									  @RequestParam String code)
			throws Exception {
		log.info("账号: " + uid + "开始找回密码");
		Cache.ValueWrapper valueWrapper = cache.get(token);
		if(Objects.isNull(valueWrapper)) {
			return Result.fail().add(EXCEPTION, CODE_EXPIRED);
		}
		String ss = (String) valueWrapper.get();
		if(!ss.equals(code)) {
			return Result.fail().add(EXCEPTION, CODE_ERROR);
		}
		boolean b = userInformationService.modifyThePassword(uid, password);
		if(!b) {
			return Result.fail().add(EXCEPTION, NETWORK_BUSY);
		}
		log.info("账号: " + uid + "找回成功, 新密码为: " + HashUtils.getPasswordHash(uid, password));
		return Result.success().add(EXCEPTION, "找回成功");
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
	public Result getModify() throws Exception{
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		log.info("用户: " + user.getUid() + "修改密码开始");
		MyEmail myEmail = myEmailService.selectMyEmailByUid(user.getUid());
		log.info("信息获取成功");
		return Result.success().add("myEmail", myEmail);
	}
	
	/**
	 * 获取验证码
	 * @param email 前端获取 
	 * @return
	 */
	@RequestMapping(value = "/getcodeformodify", method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin
	public Result sendEmailForModify(@RequestParam String email) throws Exception{
		log.info("邮箱: " + email + "开始发送修改密码的验证码");
		EmailUtils utils = new EmailUtils();
		utils.sendEmailForModify(email, ShiroUtils.getUserInformation().getUsername());
		if(Objects.isNull(cache)) {
			cache = cacheManager.getCache("code");
		}
		String token = RandomUtils.getUUID();
		String text = utils.getText();
		cache.put(token, text);
		log.info("邮箱: " + email + "发送验证码成功, 验证码为: " + text);
		return Result.success().add("token", token);
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
	public Result modifyBefore(@RequestParam String token, @RequestParam String code) throws Exception{
		log.info("开始验证验证码是否正确");
		Cache.ValueWrapper valueWrapper = cache.get(token);
		if(Objects.isNull(valueWrapper)) {
			log.info("验证码已过期, key 为: " + token);
			return Result.fail().add(EXCEPTION, "验证码已过期");
		}
		if(!valueWrapper.get().equals(code)) {
			log.info("验证码错误, 服务器端验证码为: " + valueWrapper.get() + ", 用户输入为: " + code);
			return Result.fail().add(EXCEPTION, "验证码错误");
		}
		log.info("验证码: " + code + "正确");
		return Result.success();
	}
	
	/**
	 * 修改密码
	 * @param password 新密码
	 * @return
	 */
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	@CrossOrigin
	@ResponseBody
	public Result modifyForPassword(@RequestParam String password) throws Exception{
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		Integer uid = user.getUid();
		log.info("账号: " + uid + "开始修改密码");
		boolean flag = userInformationService.modifyThePassword(
				ShiroUtils.getUserInformation().getUid(), password);
		if(!flag) {
			log.info("账号: " + uid + " 修改密码失败");
			return Result.fail().add(EXCEPTION, NETWORK_BUSY);
		}
		log.info("账号: " + uid + "修改密码成功, 新密码为: " + HashUtils.getPasswordHash(uid, password));
		return Result.success();
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
	public Result like(@RequestParam Integer uid) throws Exception{
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		Integer likeId = user.getUid();
		log.info("用户: " + likeId + ", 关注: " + uid + "开始");
		log.info("验证用户是否已关注此用户");
		boolean flag = myLikeService.insert(new MyLike(null, likeId, uid));
		if(flag) {
			log.info("用户: " + likeId + ", 关注: " + uid + "成功");
			return Result.success();
		}
		log.warn("用户: " + likeId + ", 关注: " + uid + "失败, 原因: 数据库连接失败");
		return Result.fail().add(EXCEPTION, NETWORK_BUSY);
	}
	
	/**
	 * 处理取消关注请求, 作者的学号由前端获取, 关注人学号从session里获取
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = "/delike", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result delike(@RequestParam Integer uid) throws Exception{
		UserInformation user = ShiroUtils.getUserInformation();
		Integer likeId = user.getUid();
		log.info("用户: " + likeId + ", 取消关注: " + uid + "开始");
		boolean b = myLikeService.deleteByLikeIdAndUid(likeId, uid);
		if(b) {
			log.info("用户: " + likeId + ", 取消关注: " + uid + "成功");
			return Result.success();
		}
		log.info("用户: " + likeId + ", 取消关注: " + uid + "失败, 原因: 数据库连接失败");
		return Result.fail().add(EXCEPTION, NETWORK_BUSY);
	}
	
	/**
	 * 关注状态
	 * @param uid
	 * @return
	 */
	@GetMapping("/likestatus")
	@ResponseBody
	@CrossOrigin
	public Result getLikeStatus(@RequestParam Integer uid) throws Exception{
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		Integer likeId = user.getUid();
		log.info("判断用户是否已关注某用户");
		boolean b = myLikeService.countByUidAndLikeId(likeId, uid);
		return Result.success().add("status", b);
	}
	

	/**
	 * 更换头像
	 * @param img  上传头像文件
	 * @return
	 */
	@RequestMapping(value = "/changeavatar", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result updateImage(@RequestParam("img")MultipartFile img) throws Exception {
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		log.info("用户: " + user.getUid() + " 更换头像开始");
		String name = HashUtils.getFileNameForHash(RandomUtils.getUUID()) + SimpleUtils.getSuffixName(img.getOriginalFilename());
		File file;
		log.info("判断用户头像是否为初始头像");
		if (user.getImgpath() != null && !user.getImgpath().equals(Default_User_Img_Name)) {
			log.info("用户头像为非初始头像, 删除该头像");
			file = new File(User_Img_FilePath + user.getImgpath());
			file.delete();
		} else {
			log.info("用户头像为初始头像");
		}
		file = new File(User_Img_FilePath + name);
		log.info("写入新头像, 头像名为: " + name);
		FileUtils.writeByteArrayToFile(file, img.getBytes());
		log.info("用户: " + user.getUid() + " 写入新头像成功, 开始写入数据库地址");
		boolean b = myImageService.update(new MyImage(user.getUid(), name));
		if(b) {
			log.info("用户: " + user.getUid() + " 更换头像成功");
			user.setImgpath(name);
			return Result.success().add("imgpath", name);
		}
		log.warn("更换头像时, 写入数据库头像地址失败, 原因: 连接数据库失败");
		return Result.fail().add(EXCEPTION, NETWORK_BUSY);
	}
	
	/**
	 * 修改用户信息
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/changeinfor", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Result updateUserInformation(UserInformation user) throws Exception{
		UserInformation userinfor = ShiroUtils.getUserInformation();
		loginOrNotLogin(userinfor);
		log.info("用户: " + userinfor.getUid() + " 修改用户信息");
		log.info("原信息为: " + userinfor);
		boolean b = userInformationService.updateUserInformation(userinfor.getUid(), user);
		if(b) {
			if(!StringUtils.isEmpty(user.getUsername())) {
				userinfor.setUsername(user.getUsername());
			}
			if(!StringUtils.isEmpty(user.getClazz())) {
				userinfor.setClazz(user.getClazz());
			}
			if(!StringUtils.isEmpty(user.getCollege())) {
				userinfor.setCollege(user.getCollege());
			}
			log.info("用户: " + userinfor.getUid() + " 修改信息成功, 修改后的信息为: " + userinfor);
			return Result.success().add("user", userinfor);
		}
		log.warn("用户: " + userinfor.getUid() + "修改信息失败, 原因: 连接数据库失败");
		return Result.fail().add(EXCEPTION, NETWORK_BUSY);
	}
	
	
	/**
	 * 获取自己的博客
	 * @return
	 */
	@RequestMapping(value = "/getoneselfblobs", method = RequestMethod.GET)
	@CrossOrigin
	@ResponseBody
	public Result getOneselfBlobsByUid(@RequestParam(value = "p", defaultValue = "1")Integer p) throws Exception{
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		log.info("用户: " + user.getUid() + "获取自己的文章开始");
		PageHelper.startPage(p, 15);
		List<WebInformation> weblist = webInformationService.selectWebInformationsByUid(user.getUid());
		List<Integer> webids = new ArrayList<Integer>();
		for (WebInformation web : weblist) {
			webids.add(web.getId());
		}
		List<Integer> visitList = visitInformationService.selectVisitsByWebIds(webids);
		List<BlobInformation> blobList = new ArrayList<>();
		for(int i = 0; i < weblist.size(); i++) {
			blobList.add(new BlobInformation(
					user, weblist.get(i), visitList.get(i), null, null
			));
		}
		PageInfo<BlobInformation> blobs = new PageInfo<>(blobList);
		return Result.success().add("blobList", blobs);
	}
	
	/**
	 * 获取自己的粉丝
	 * @param p
	 * @return
	 */
	@CrossOrigin
	@GetMapping("/getoneselffans")
	@ResponseBody
	public Result getOneselfFans(@RequestParam(value = "p", defaultValue = "1") Integer p) throws Exception{
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		log.info("用户: " + user.getUid() + " 获取自己的粉丝信息");
		log.info("获取粉丝信息");
		PageHelper.startPage(p, 30);
		List<UserInformation> users = userInformationService.selectUsersByLikeId(user.getUid());
		List<Integer> uids = new ArrayList<>();
		for(UserInformation userinfor : users) {
			UserInformation u = userinfor;
			u.setPassword(null);
			uids.add(u.getUid());
		}
		log.info("获取是否互关");
		List<Boolean> islikes = myLikeService.countByUidAndLikeId(user.getUid(), uids);
		log.info("获取粉丝头像");
		List<MyImage> imgs = myImageService.selectImagePathByUids(uids);
		List<FansInformation> fans = new ArrayList<>();
		for(Integer i = 0; i < users.size(); i++) {
			UserInformation u = users.get(i);
			for(MyImage img : imgs) {
				if(img.getUid().equals(u.getUid())) {
					u.setImgpath(img.getImagePath());
					break;
				}
			}
			fans.add(new FansInformation(u, islikes.get(i)));
		}
		PageInfo<FansInformation> userList = new PageInfo<FansInformation>(fans);
		log.info("获取粉丝信息成功");
		return Result.success().add("fansList", userList);
	}
	
	/**
	 * 获取自己的关注
	 * @param p
	 * @return
	 */
	@CrossOrigin
	@GetMapping("/getoneselficons")
	@ResponseBody
	public Result getOneselfIcons(@RequestParam(value = "p", defaultValue = "1") Integer p) throws Exception{
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		log.info("用户: " + user.getUid() + " 获取自己的关注人信息");
		log.info("获取关注人信息");
		PageHelper.startPage(p, 30);
		List<UserInformation> users = userInformationService.selectUsersByUid(user.getUid());
		log.info("获取关注人学号");
		List<Integer> uids = new ArrayList<>();
		for(UserInformation userinfor : users) {
			UserInformation u = userinfor;
			u.setPassword(null);
			uids.add(u.getUid());
		}
		log.info("获取关注人头像");
		List<MyImage> imgs = myImageService.selectImagePathByUids(uids);
		for(UserInformation userinfor : users) {
			UserInformation u = userinfor;
			for(MyImage img : imgs) {
				if(img.getUid().equals(u.getUid())) {
					u.setImgpath(img.getImagePath());
					break;
				}
			}
		}
		PageInfo<UserInformation> userList = new PageInfo<UserInformation>(users);
		log.info("获取关注人信息成功");
		return Result.success().add("userList", userList);
	}
	
	/**
	 * 获取用户的博客
	 * @return
	 */
	@RequestMapping(value = "/getblobs", method = RequestMethod.GET)
	@CrossOrigin
	@ResponseBody
	public Result getBlobsByUid(@RequestParam(value = "p", defaultValue = "1")Integer p, @RequestParam Integer uid)
			throws Exception{
		log.info("获取用户: " + uid + " 的文章开始");
		PageHelper.startPage(p, 15);
		List<WebInformation> weblist = webInformationService.selectWebInformationsByUid(uid);
		List<Integer> webids = new ArrayList<Integer>();
		for (WebInformation web : weblist) {
			webids.add(web.getId());
		}
		log.info("获取访问量");
		List<Integer> visitList = visitInformationService.selectVisitsByWebIds(webids);
		log.info("获取用户信息");
		UserInformation user = userInformationService.selectByUid(uid);
		log.info("获取用户头像");
		MyImage img = myImageService.selectImagePathByUid(uid);
		user.setPassword(null);
		user.setImgpath(img.getImagePath());
		List<BlobInformation> blobList = new ArrayList<BlobInformation>();
		for(int i = 0; i < weblist.size(); i++) {
			blobList.add(new BlobInformation(
					user, weblist.get(i), visitList.get(i), null, null
					));
		}
		PageInfo<BlobInformation> blobs = new PageInfo<BlobInformation>(blobList);
		return Result.success().add("blobList", blobs);
	}
	
	/**
	 * 获取用户粉丝
	 * @param p
	 * @return
	 */
	@CrossOrigin
	@GetMapping("/getfans")
	@ResponseBody
	public Result getFans(@RequestParam(value = "p", defaultValue = "1") Integer p, @RequestParam Integer uid) throws Exception{
		log.info("用户: " + uid + " 获取自己的粉丝信息");
		log.info("获取粉丝信息");
		PageHelper.startPage(p, 30);
		List<UserInformation> users = userInformationService.selectUsersByLikeId(uid);
		log.info("获取粉丝学号");
		List<Integer> uids = new ArrayList<>();
		for(UserInformation userinfor : users) {
			UserInformation u = userinfor;
			u.setPassword(null);
			uids.add(u.getUid());
		}
		log.info("获取是否互关");
		List<Boolean> islikes = myLikeService.countByUidAndLikeId(uid, uids);
		log.info("获取粉丝头像");
		List<MyImage> imgs = myImageService.selectImagePathByUids(uids);
		List<FansInformation> fans = new ArrayList<>();
		for(Integer i = 0; i < users.size(); i++) {
			UserInformation u = users.get(i);
			for(MyImage img : imgs) {
				if(img.getUid().equals(u.getUid())) {
					u.setImgpath(img.getImagePath());
					break;
				}
			}
			fans.add(new FansInformation(u, islikes.get(i)));
		}
		PageInfo<FansInformation> userList = new PageInfo<FansInformation>(fans);
		log.info("获取粉丝信息成功");
		return Result.success().add("fansList", userList);
	}
	
	/**
	 * 获取用户的关注
	 * @param p
	 * @return
	 */
	@CrossOrigin
	@GetMapping("/geticons")
	@ResponseBody
	public Result getIcons(@RequestParam(value = "p", defaultValue = "1") Integer p, @RequestParam Integer uid) throws Exception{
		log.info("获取用户: " + uid + " 的关注人信息");
		log.info("获取关注人信息");
		PageHelper.startPage(p, 30);
		List<UserInformation> users = userInformationService.selectUsersByUid(uid);
		log.info("获取关注人学号");
		List<Integer> uids = new ArrayList<>();
		for(UserInformation userinfor : users) {
			UserInformation u = userinfor;
			u.setPassword(null);
			uids.add(u.getUid());
		}
		log.info("获取关注人头像");
		List<MyImage> imgs = myImageService.selectImagePathByUids(uids);
		for(UserInformation userinfor : users) {
			UserInformation u = userinfor;
			for(MyImage img : imgs) {
				if(img.getUid().equals(u.getUid())) {
					u.setImgpath(img.getImagePath());
					break;
				}
			}
		}
		PageInfo<UserInformation> userList = new PageInfo<UserInformation>(users);
		log.info("获取关注人信息成功");
		return Result.success().add("userList", userList);
	}
	
	/**
	 * 获取登录状态用户的邮箱
	 * @return
	 */
	@RequestMapping("/loginemail")
	@ResponseBody
	@CrossOrigin
	public Result getEmailByUid() throws Exception{
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		log.info("获取用户: " + user.getUid() + " 邮箱");
		MyEmail myEmail = myEmailService.selectMyEmailByUid(user.getUid());
		if(myEmail == null) {
			log.info("获取失败, 该用户未绑定邮箱");
			return Result.fail().add(EXCEPTION, "该用户未绑定邮箱");
		}
		String email = SimpleUtils.getAsteriskForString(myEmail.getEmail());
		return Result.success().add("email", email);
	}
	
	/**
	 * 数据校验
	 * @param data  需要校验的数据
	 * @param type  数据校验的类型
	 * 		取值为 4 种
	 * 			1. uid 校验学号
	 * 			2. username 校验用户名
	 * 			3. password 校验密码
	 * 			4. email 校验邮箱
	 * @return
	 */
	@ResponseBody
	@GetMapping("/datacheck")
	@CrossOrigin
	public Result dataCheck(@RequestParam String data, @RequestParam String type) {
		boolean b;
		switch (type) {
			case "uid":
				b = data.matches("^\\d+$");
				if(b) {
					int countByUid = userInformationService.countByUid(Integer.parseInt(data));
					if(countByUid == 0) {
						return Result.success();
					}
					return Result.fail().add(EXCEPTION, "该学号已被使用");
				}
				return Result.fail().add(EXCEPTION, "学号或工号应为纯数字");
			case "username":
				 b = data.matches("[\\u4e00-\\u9fa5_a-zA-Z0-9]{2,16}");
				 if(b) {
					 int countByUserName = userInformationService.countByUserName(data);
					 if(countByUserName == 0) {
						 return Result.success();
					 }
					 return Result.fail().add(EXCEPTION, "用户名已存在");
				 }
				 return Result.fail().add(EXCEPTION, "用户名为4~16英文字符, 数字, 或2~8个汉字");
			case "password":
				b = data.matches("([0-9A-Za-z\\[\\](){}!@#$%^&*,./;':\"<>\\?|`~+-_=]){6,20}");
				if(b) {
					b = data.matches("^\\d+$");
					if(b) {
						return Result.fail().add(EXCEPTION, "密码不可为纯数字");
					}
					return Result.success();
				}
				return Result.fail().add(EXCEPTION, "密码必须为6~20位字母, 数字, 字符的组合");
			case "email":
				b = data.matches("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
				if(b) {
					boolean c = myEmailService.countByEmail(data);
					if(!c) {
						return Result.success();
					}
					return Result.fail().add(EXCEPTION, "邮箱已被使用");
				}
				return Result.fail().add(EXCEPTION, "邮箱不正确");
			default:
				return Result.fail().add(EXCEPTION, "数据类型不正确");
		}
	}
	
	
	/**
	 * 获取自己的浏览记录
	 * @return
	 */
	@ResponseBody
	@GetMapping("/getoneselfbrowsingrecord")
	@CrossOrigin
	public Result getBrowsingRecord(@RequestParam(defaultValue = "1") Integer p) throws Exception{
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		log.info("获取访问记录信息");
		PageHelper.startPage(p,15);
		List<UserBrowsingRecord> userBrowsingRecords = userBrowsingRecordService.selectBrowsingRecordByUid(user.getUid());
		List<Integer> webids = new ArrayList<>();
		List<Integer> fileids = new ArrayList<>();
		log.info("获取访问过的博客或文件ID");
		for (UserBrowsingRecord brows : userBrowsingRecords) {
			if(brows.getTpye() == 1) {
				webids.add(brows.getWfid());
			} else {
				fileids.add(brows.getWfid());
			}
		}
		log.info("获取博客信息");
		List<WebInformation> webs = webInformationService.selectWebInformationsByIds(webids, false);
		List<Integer> uids = new ArrayList<>();
		for(WebInformation web : webs) {
			uids.add(web.getUid());
		}

		log.info("获取作者信息");
		List<UserInformation> users = userInformationService.selectUsersByUids(uids);
		log.info("获取文件信息");
		List<WebFile> files = webFileService.selectFilesByFileIds(fileids);
		log.info("拼装访问信息");
		List<BrowsingRecordInformation> browsingRecordInformations = new ArrayList<>();
		for (Integer i = 0; i < userBrowsingRecords.size(); i++) {
			BrowsingRecordInformation browsingRecordInformation = new BrowsingRecordInformation();
			UserBrowsingRecord record = userBrowsingRecords.get(i);
			browsingRecordInformation.setCreatetime(SimpleUtils.getSimpleDateDifferenceFormat(record.getCreatetime()));
			browsingRecordInformation.setWfid(record.getWfid());
			if(record.getTpye() == 1) {
				browsingRecordInformation.setType(1);
				for(WebInformation webInformation : webs) {
					if(webInformation.getId().equals(record.getWfid())) {
						browsingRecordInformation.setTitle(webInformation.getTitle());
						browsingRecordInformation.setUid(webInformation.getUid());
						break;
					}
				}
				for(UserInformation u : users) {
					if(u.getUid().equals(browsingRecordInformation.getUid())) {
						browsingRecordInformation.setUsername(u.getUsername());
						break;
					}
				}
			} else {
				browsingRecordInformation.setType(2);
				for(WebFile file : files) {
					if(file.getId().equals(record.getWfid())) {
						browsingRecordInformation.setTitle(file.getTitle());
						browsingRecordInformation.setUid(file.getUid());
						break;
					}
				}
			}
			browsingRecordInformations.add(browsingRecordInformation);
		}
		log.info("获取浏览历史信息成功");
		PageInfo<BrowsingRecordInformation> pageInfo = new PageInfo<>(browsingRecordInformations);
		return Result.success().add("brows", pageInfo);
	}

	/**
	 * 获取自己的通知
	 * @param p
	 * @return
	 */
	@GetMapping("/getnotification")
	@ResponseBody
	@CrossOrigin
	public Result getOneselfNotification(@RequestParam(defaultValue = "1") Integer p) throws Exception {
		UserInformation user = ShiroUtils.getUserInformation();
		loginOrNotLogin(user);
		log.info("用户: " + user.getUid() + " 获取通知开始");
		log.info("获取通知");
		PageHelper.startPage(p, 10);
		List<SystemNotification> systemNotifications = systemNotificationService.selectSystemNotificationsByUid(user.getUid());
		List<Integer> uids = new ArrayList<>();
		log.info("获取通知人信息");
		for (SystemNotification s : systemNotifications) {
			uids.add(s.getSid());
		}
		List<UserInformation> users = userInformationService.selectUsersByUids(uids);
		log.info("拼装信息");
		List<SystemNotificationInformation> list = new ArrayList<>();
		for (SystemNotification s : systemNotifications) {
			SystemNotificationInformation information = new SystemNotificationInformation();
			information.setSystemNotification(s);
			for (UserInformation u : users) {
				if (s.getSid().equals(u.getUid())) {
					information.setUsername(u.getUsername());
					information.setIdentity("管理员");
				}
			}
			list.add(information);
		}
		boolean b = systemNotificationService.updateSystemNotificationsByUid(user.getUid());
		if (b) {
			user.setSystemNotifications(0);
		}
		PageInfo<SystemNotificationInformation> pageInfo = new PageInfo<>(list);
		return Result.success().add("notificationList", pageInfo);
	}

}

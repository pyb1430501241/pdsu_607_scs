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
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pdsu.scs.bean.MyEmail;
import com.pdsu.scs.bean.Result;
import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.service.MyEmailService;
import com.pdsu.scs.service.UserInformationService;
import com.pdsu.scs.utils.CodeUtils;
import com.pdsu.scs.utils.EmailUtils;
import com.pdsu.scs.utils.ShiroUtils;
import com.pdsu.scs.utils.SimpleDateUtils;


@Controller
@RequestMapping("/user")
public class WebHanlder {
	
	private static final String ex = "exception";
	
	@Autowired
	private UserInformationService userInformationService;
	
	@Autowired
	private MyEmailService myEmailService;

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
	 * 处理登录
	 * @param uid 账号
	 * @param password 密码
	 * @param hit cache获取数据的key
	 * @param code 输入的验证码
	 * @param flag  是否记住密码 默认为不记住
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/login")
	@CrossOrigin
	public Result loginAjax(String uid, String password, String hit, String code, 
			@RequestParam(value = "flag", defaultValue = "0")Integer flag) {
		Subject subject = SecurityUtils.getSubject();
		if(cache.get(hit) == null) {
			return Result.fail().add(ex, "验证码已失效, 请刷新后重试");
		}
		//从缓存中获取验证码
		String ss = (String) cache.get(hit).get();
		if(!ss.equals(code)) {
			return Result.fail().add(ex, "验证码错误");
		}
		/**
		 * 如果未认证
		 */
		if(!subject.isAuthenticated()) {
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
					return Result.fail().add(ex, "账号被冻结");
				}
				if(uu.getAccountStatus() == 3) {
					return Result.fail().add(ex, "账号被封禁");
				}
				if(uu.getAccountStatus() == 4) {
					return Result.fail().add(ex, "账号不存在");
				}
				return Result.success().add(ex, "登录成功")
						.add("sessionId", subject.getSession().getId())
						.add("user", uu);
			}catch (IncorrectCredentialsException e) {
				return Result.fail().add(ex, "账号或密码错误");
			}catch (UnknownAccountException e) {
				return Result.fail().add(ex, "账号不存在");
			}catch (Exception e) {
				return Result.fail().add(ex, "未知错误");
			}
		}
		return Result.fail().add(ex, "你已登录");
	}
	
	/**
	 * 获取验证码
	 * @param session 储存验证码到缓存区 cache
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getCodeForLogin")
	@CrossOrigin
	public Result getCode(){
		try {
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
			System.out.println(utils.getText());
			return Result.success().add("img", src).add("token", token);
		} catch (IOException e) {
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
			if(email == null) {
				return Result.fail().add(ex, "邮箱不可为空");
			}
			if(name == null) {
				return Result.fail().add(ex, "用户名不可为空");
			}
			if(myEmailService.countByEmail(email) != 0) {
				return Result.fail().add(ex, "此邮箱已被绑定");
			}
			EmailUtils utils = new EmailUtils();
			utils.sendEmailForApply(email, name);
			String text = utils.getText();
			if(cache == null) {
				cache = cacheManager.getCache("code");
			}
			String token = UUID.randomUUID().toString();
			cache.put(token, text);
			return Result.success().add("token", token);
		}catch (Exception e) {
			return Result.fail().add(ex, "邮箱地址不正确");
		}
	}
	
	/**
	 * 申请账号
	 * @param user  POJO 类
	 * @param email 邮箱
	 * @param token 获取验证码的key
	 * @param code  前端输入验证码
	 * @return json字符串
	 */
	@RequestMapping("/applyNumber")
	@ResponseBody
	@CrossOrigin
	public Result applyforAccountNumber(UserInformation user, String email, String token, String code) {
		try {
			//验证验证码
			if(cache.get(token) == null) {
				return Result.fail().add(ex, "验证码已过期");
			}
			String ss = (String) cache.get(token).get();
			if(!ss.equals(code)) {
				return Result.fail().add(ex, "验证码错误");
			}
			//查询账号是否已存在
			if(userInformationService.countByUid(user.getUid()) != 0) {
				return Result.fail().add(ex, "该账号已存在,是否忘记密码?");
			}
			if(myEmailService.countByEmail(email) != 0) {
				return Result.fail().add(ex, "此邮箱已被绑定, 忘记密码?");
			}
			//默认账号为正常状态
			user.setAccountStatus(1);
			//设置申请时间
			user.setTime(SimpleDateUtils.getSimpleDate());
			//设置默认头像
			user.setImgpath("01.png");
			boolean flag = userInformationService.inset(user);
			if(flag) {
				return Result.success().add(ex, "申请成功");
			}else {
				return Result.fail().add(ex, "申请失败");
			}
		}catch (Exception e) {
			return Result.fail().add(ex, "连接服务器失败, 请稍候重试");
		}
	}
	
	/**
	 * 修改密码API集
	 */
	/**
	 *  该API为用户输入邮箱验证此用户是否存在
	 * @param email
	 * @return
	 */
	@RequestMapping("/inputEmail")
	@ResponseBody
	@CrossOrigin
	public Result inputEmail(String email) {
		try {
			int i = myEmailService.countByEmail(email);
			if(i != 0) {
				return Result.success().add("email", myEmailService.selectMyEmailByEmail(email));
			}else {
				return Result.fail();
			}
		}catch (Exception e) {
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
			System.out.println(text);
			return Result.success().add("token", token);
		}catch (Exception e) {
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
			if(cache.get(token) == null) {
				return Result.fail().add(ex, "验证码已过期");
			}
			String ss = (String) cache.get(token).get();
			if(!ss.equals(code)) {
				return Result.fail().add(ex, "验证码错误"); 
			}
			boolean b = userInformationService.ModifyThePassword(uid, password);
			if(!b) {
				return Result.fail().add(ex, "密码修改失败, 请稍后重试");
			}
			return Result.success().add(ex, "修改成功");
		}catch (Exception e) {
			return Result.fail().add(ex, "未知错误");
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
			UserInformation user = ShiroUtils.getUserInformation();
			MyEmail myEmail = myEmailService.selectMyEmailByUid(user.getUid());
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
			EmailUtils utils = new EmailUtils();
			utils.sendEmailForModify(email, ShiroUtils.getUserInformation().getUsername());
			if(cache == null) {
				cache = cacheManager.getCache("code");
			}
			String token = UUID.randomUUID().toString();
			String text = utils.getText();
			cache.put(token, text);
			return Result.success().add("token", token);
		}catch (Exception e) {
			return Result.fail().add(ex, "未知错误");
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
			if(cache.get(token) == null) {
				return Result.fail().add(ex, "验证码已过期");
			}
			if(!cache.get(token).get().equals(code)) {
				return Result.fail().add(ex, "验证码错误");
			}
			return Result.success();
		}catch (Exception e) {
			return Result.fail().add(ex, "未知错误");
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
		try {
			boolean flag = userInformationService.ModifyThePassword(
					ShiroUtils.getUserInformation().getUid(), password);
			if(!flag) {
				return Result.fail().add(ex, "修改失败");
			}
			return Result.success();
		}catch (Exception e) {
			return Result.fail().add(ex, "未知错误");
		}
	}
}

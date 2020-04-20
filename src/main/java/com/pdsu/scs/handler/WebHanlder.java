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

import com.pdsu.scs.bean.Result;
import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.service.UserInformationService;
import com.pdsu.scs.utils.CodeUtils;


@Controller
public class WebHanlder {
	
	@Autowired
	private UserInformationService userInformationService;

	/**
	 * 缓存管理器
	 */
	@Autowired
	private CacheManager cacheManager;
	
	/**
	 * 缓存区, 用于存放验证码, 验证码有效期为 60 秒
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
	 * @param hit session获取数据的key
	 * @param code 输入的验证码
	 * @param flag  是否记住密码 默认为不记住
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/loginAjax")
	@CrossOrigin
	public Result loginAjax(String uid, String password, String hit, String code, 
			@RequestParam(value = "flag", defaultValue = "0")Integer flag) {
		Subject subject = SecurityUtils.getSubject();
		if(cache.get(hit) == null) {
			return Result.fail().add("exception", "验证码已失效, 请刷新后重试");
		}
		//从缓存中获取验证码
		String ss = (String) cache.get(hit).get();
		if(!ss.equals(code)) {
			return Result.fail().add("exception", "验证码错误");
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
				if(uu.getAccountStatus() == 2) {
					return Result.fail().add("exception", "账号被冻结");
				}
				if(uu.getAccountStatus() == 3) {
					return Result.fail().add("exception", "账号被封禁");
				}
				if(uu.getAccountStatus() == 4) {
					return Result.fail().add("exception", "账号不存在");
				}
				return Result.success().add("exception", "登录成功")
						.add("sessionId", subject.getSession().getId())
						.add("user", uu);
			}catch (IncorrectCredentialsException e) {
				return Result.fail().add("exception", "账号或密码错误");
			}catch (UnknownAccountException e) {
				return Result.fail().add("exception", "账号不存在");
			}catch (Exception e) {
				return Result.fail().add("exception", "未知错误");
			}
		}
		return Result.fail().add("exception", "你已登录");
	}
	
	/**
	 * 获取验证码
	 * @param session 储存验证码到 session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getCode")
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
			return Result.success().add("img", src).add("token", token);
		} catch (IOException e) {
			return Result.fail();
		}
	}
}

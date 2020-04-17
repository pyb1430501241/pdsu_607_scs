package com.pdsu.scs.handler;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pdsu.scs.bean.Result;
import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.service.UserInformationService;

@Controller
public class WebHanlder {
	
	@Autowired
	private UserInformationService userInformationService;
	
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	/**
	 * 处理登录
	 * @param user  POJO
	 * @param flag  是否记住密码
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/loginAjax")
	@CrossOrigin
	public Result loginAjax(UserInformation user, @RequestParam(value = "flag", defaultValue = "0")Integer flag,
			HttpServletResponse response) {
		Subject subject = SecurityUtils.getSubject();
		if(!subject.isAuthenticated()) {
			UsernamePasswordToken token = new UsernamePasswordToken(user.getUid()+"", user.getPassword());
			if(flag == 0) {
				token.setRememberMe(false);
			}else {
				token.setRememberMe(true);
			}
			try{
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
	 * 获取用户信息
	 * @param uid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getUser")
	public Result getUserForSession(Integer uid) {
		return Result.success().add("user", userInformationService.selectByUid(uid));
	}
}

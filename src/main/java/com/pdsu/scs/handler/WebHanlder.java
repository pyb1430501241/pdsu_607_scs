package com.pdsu.scs.handler;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pdsu.scs.Exception.UserExpection;
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
	
	@ResponseBody
	@RequestMapping("/loginAjax")
	public Result loginAjax(UserInformation user, HttpSession session) {
		Subject subject = SecurityUtils.getSubject();
		boolean flag = false;
		if(!subject.isAuthenticated()) {
			UsernamePasswordToken token = new UsernamePasswordToken(user.getUid()+"", user.getPassword());
			token.setRememberMe(false);
			try{
				subject.login(token);
				UserInformation tt = (UserInformation) subject.getPrincipal();
				if(tt.getAccountStatus() != 1) {
					flag = true;
					 throw new UserExpection("账号被冻结");
				}
			}catch (UnknownAccountException e) {
				flag = true;
                throw new UserExpection("用户名不存在");
            }catch (IncorrectCredentialsException e) {
            	flag = true;
            	 throw new UserExpection("密码错误");
            }catch (AuthenticationException  e) {
            	flag = true;
                throw new UserExpection("未知错误");
            }
		}
		session.setAttribute("user", userInformationService.selectByUid(user.getUid()));
		return flag ? Result.fail() : Result.success();
	}
	
}

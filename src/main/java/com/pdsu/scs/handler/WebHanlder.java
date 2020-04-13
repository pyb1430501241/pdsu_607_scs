package com.pdsu.scs.handler;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
	
	@RequestMapping("/loginAjax")
	public String loginAjax(UserInformation user) {
		Subject subject = SecurityUtils.getSubject();
		if(!subject.isAuthenticated()) {
			UsernamePasswordToken token = new UsernamePasswordToken(user.getUid()+"", user.getPassword());
			token.setRememberMe(true);
			System.out.println(token.getPassword());
			System.out.println(token.getUsername());
			try{
				subject.login(token);
			}catch (Exception e) {
				System.out.println("登陆失败" + e.getMessage());
			}
		}
		return "redirect:blob/index";
	}
	
}

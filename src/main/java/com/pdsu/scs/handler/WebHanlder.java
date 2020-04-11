package com.pdsu.scs.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public String loginAjax(String username, String password) {
		System.out.println(username + "---->" + password);
		return "login";
	}
	
}

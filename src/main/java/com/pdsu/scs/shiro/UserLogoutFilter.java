package com.pdsu.scs.shiro;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pdsu.scs.utils.ShiroUtils;

/**
 * 退出请求拦截器
 * @author 半梦
 *
 */
public class UserLogoutFilter extends LogoutFilter{

	 private static final Logger log = LoggerFactory.getLogger(UserLogoutFilter.class);
	
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) {
		Subject subject = getSubject(request, response);
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			return false;
		}
		response.setCharacterEncoding("UTF-8");
        JSONObject json = new JSONObject();
		try {
			log.info("用户: " + ShiroUtils.getUserInformation().getUid() + ", 退出登录");
	        subject.logout();
	    } catch (Exception ise) {
	    	json.append("code", 404);
	        json.append("msg", "fail");
	        out.println(json);
	        out.flush();
	        out.close();
	        log.error("用户退出登录失败, 原因: " + ise.getMessage());
	        return false;
	    }
        json.append("code", 200);
        json.append("msg", "success");
        out.println(json);
        out.flush();
        out.close();
        log.info("用户退出登录成功");
	    return false;
	}
	
}

package com.pdsu.scs.shiro;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.shiro.session.SessionException;
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
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		Subject subject = getSubject(request, response);
	    log.info("用户: " + ShiroUtils.getUserInformation().getUid() + " 退出登录");
		try {
	        subject.logout();
	    } catch (SessionException ise) {
	        log.error("用户退出登录失败, 原因: " + ise.getMessage());
	    }
	    response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject json = new JSONObject();
        json.append("code", 200);
        json.append("msg", "success");
        out.println(json);
        out.flush();
        out.close();
        log.info("退出登录成功");
	    return false;
	}
	
}

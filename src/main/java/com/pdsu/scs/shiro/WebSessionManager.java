package com.pdsu.scs.shiro;

import java.io.Serializable;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import com.pdsu.scs.utils.SimpleUtils;

/**
 * Session 管理器, 在用户登录后, 从请求头中获取 sessionId, 以确保每次访问的 Session 都是同一Session
 * @author 半梦
 *
 */
public class WebSessionManager extends DefaultWebSessionManager{
	
	public static final String AUTHORIZATION = "Authorization";
	
	private static final String REFERENCED_SESSION_ID_SOURCE  = "Stateless request";
	
	
	public WebSessionManager() {
		setGlobalSessionTimeout(SimpleUtils.CSC_WEEK);
	}
	
	@Override
	protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
		String sessionId = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
		/**
		 * 如果请求头中有 sessionId
		 */
		if(!StringUtils.isEmpty(sessionId)) {
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sessionId);
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
			return sessionId;
		}
		/**
		 * 如果没有则调用父类方法
		 */
		return super.getSessionId(request, response);
	}
}

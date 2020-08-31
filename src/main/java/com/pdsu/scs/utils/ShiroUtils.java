package com.pdsu.scs.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import com.pdsu.scs.bean.UserInformation;

/**
 *  shiro 框架工具类
 * @author 半梦
 *
 */
public class ShiroUtils {

	public static UserInformation getUserInformation() {
        Subject subject = SecurityUtils.getSubject();
        //取出身份信息
        UserInformation userInformation = (UserInformation) subject.getPrincipal();
        if(userInformation != null){
        	Session session = subject.getSession();
            UserInformation user = (UserInformation) session.getAttribute("user");
            if(user == null){
                session.setAttribute("user", userInformation);
            }
            return userInformation;
        }else{
            return null;
        }
	}
	
	 /**
	  * 根据 sessionid 获取用户信息
     * @param sessionID
     * @param request
     * @param response
     * @return
     */
    public static UserInformation getUserInformation(String sessionID,HttpServletRequest request,HttpServletResponse response) throws Exception{
        SessionKey key = new WebSessionKey(sessionID,request,response);
        Session se = SecurityUtils.getSecurityManager().getSession(key);
        Object obj = se.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        SimplePrincipalCollection coll = (SimplePrincipalCollection) obj;
        UserInformation userInformation = (UserInformation)coll.getPrimaryPrincipal();
        if(userInformation!=null){
        	UserInformation user = (UserInformation) se.getAttribute("user");
            if(user==null){
                se.setAttribute("user", userInformation);
            }
            return userInformation;
        }else{
            return null;
        }
    }
}

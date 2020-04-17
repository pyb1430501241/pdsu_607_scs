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

public class ShiroUtils {

	public static UserInformation getUser() {
		  //从shiro的session中取出activeUser
        Subject subject = SecurityUtils.getSubject();
        //取出身份信息
        UserInformation activeUser = (UserInformation) subject.getPrincipal();
        if(activeUser!=null){
            Session session = subject.getSession();
            UserInformation user = (UserInformation) session.getAttribute("user");
            if(user==null){
                session.setAttribute("user", activeUser);
            }
            return activeUser;
        }else{
            return null;
        }
	}
	
	 /**
     * 根据sessionid 获取用户信息
     * @param sessionID
     * @param request
     * @param response
     * @return
     */
    public static UserInformation getActiveUser(String sessionID,HttpServletRequest request,HttpServletResponse response) throws Exception{
        SessionKey key = new WebSessionKey(sessionID,request,response);
        Session se = SecurityUtils.getSecurityManager().getSession(key);
        Object obj = se.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        //org.apache.shiro.subject.SimplePrincipalCollection cannot be cast to com.hncxhd.bywl.entity.manual.UserInfo
        SimplePrincipalCollection coll = (SimplePrincipalCollection) obj;
        UserInformation activeUser = (UserInformation)coll.getPrimaryPrincipal();

        if(activeUser!=null){
        	UserInformation user = (UserInformation) se.getAttribute("user");
            if(user==null){
                se.setAttribute("user", activeUser);
            }
            return activeUser;
        }else{
            return null;
        }
    }
	
}

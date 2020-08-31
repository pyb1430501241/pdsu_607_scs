package com.pdsu.scs.shiro;

import com.pdsu.scs.exception.web.user.UserAbnormalException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.pdsu.scs.bean.Result;
import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.service.UserInformationService;

/**
 * 
 * @author 半梦
 *
 */
public class LoginRealm extends AuthorizingRealm{

	@Autowired
	private UserInformationService userInformationService;
	
	/**
	 * 登录认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken uptoken = (UsernamePasswordToken) token;
		Integer uid = Integer.parseInt(uptoken.getUsername());
		//查询是否有此账号
		if(userInformationService.countByUid(uid) == 0) {
			return null;
		}
		UserInformation user = userInformationService.selectByUid(uid);
		if(user.getAccountStatus() == 2) {
			throw new UserAbnormalException("账号被冻结");
		}
		if(user.getAccountStatus() == 3) {
			throw new UserAbnormalException("账号被封禁");
		}
		if(user.getAccountStatus() == 4) {
			throw new UserAbnormalException("账号已注销");
		}
		Object credentials = user.getPassword();
		String realmName = getName();
		ByteSource credentialsSalt = ByteSource.Util.bytes(uid+"");
		return new SimpleAuthenticationInfo(user, credentials, credentialsSalt, realmName);
	}
	
	/**
	 * 负责权限分配
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return null;
	}

}

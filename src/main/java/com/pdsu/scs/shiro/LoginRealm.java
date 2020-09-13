package com.pdsu.scs.shiro;

import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.exception.web.user.UserAbnormalException;
import com.pdsu.scs.service.UserInformationService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

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
		if(userInformationService.countByUid(uid) == 0) {
			throw new UnknownAccountException("账号不存在");
		}
		UserInformation user = userInformationService.selectByUid(uid);
		UserInformation userInformation = UserInformation.createUserInformationByUser(user);
		if(user.getAccountStatus() == 2) {
			throw new UserAbnormalException("账号被冻结");
		}
		if(user.getAccountStatus() == 3) {
			throw new UserAbnormalException("账号被封禁");
		}
		if(user.getAccountStatus() == 4) {
			throw new UserAbnormalException("账号已注销");
		}
		Object credentials = userInformation.getPassword();
		String realmName = getName();
		ByteSource credentialsSalt = ByteSource.Util.bytes(uid+"");
		return new SimpleAuthenticationInfo(userInformation, credentials, credentialsSalt, realmName);
	}
	
	/**
	 * 负责权限分配
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return null;
	}

}

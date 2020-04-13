package com.pdsu.scs.shiro;

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

import com.pdsu.scs.Exception.UserExpection;
import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.service.UserInformationService;

public class LoginRealm extends AuthorizingRealm{

	@Autowired
	private UserInformationService userInformationService;
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken uptoken = (UsernamePasswordToken) token;
		Integer uid = Integer.parseInt(uptoken.getUsername());
		if(userInformationService.countByUid(uid) == 0) {
			throw new UserExpection("用户名不存在");
		}
		UserInformation user = userInformationService.selectByUid(uid);
		System.out.println(user.getAccountStatus());
		if(user.getAccountStatus() == 2) {
			throw new UserExpection("账号被冻结");
		}else if(user.getAccountStatus() == 3) {
			throw new UserExpection("账号被封禁");
		}else if(user.getAccountStatus() == 4) {
			throw new UserExpection("账号不存在");
		}
		Object principal = uid;
		Object credentials = user.getPassword();
		String realmName = getName();
		ByteSource credentialsSalt = ByteSource.Util.bytes(uid+"");
		return new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, realmName);
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return null;
	}

}

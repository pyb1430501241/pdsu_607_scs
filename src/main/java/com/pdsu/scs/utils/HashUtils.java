package com.pdsu.scs.utils;

import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * 该类是一个Hash加密相关的工具类 
 * @author 半梦
 *
 */
public class HashUtils {

	//根据账号密码去加密密码
	public static String getPasswordHash(Integer uid, String password) {
		
		SimpleHash simpleHash = new SimpleHash("MD5", password, uid+"", 2);
		
		return simpleHash.toString();
	}
	
}

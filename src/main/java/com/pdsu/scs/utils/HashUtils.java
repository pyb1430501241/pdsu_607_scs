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
	
	//根据文件名生成一个20位的hash名称，去代表该文件名
	public static String getFileNameForHash(String title) {
			
		SimpleHash simpleHash = new SimpleHash("MD5", title);
			
		return simpleHash.toString().substring(0, 20);
	}
	
}

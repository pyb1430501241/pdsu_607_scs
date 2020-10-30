package com.pdsu.scs.utils;

import org.springframework.lang.NonNull;

import java.util.UUID;

/**
 * 随机数生成工具
 * @author 半梦
 *
 */
public class RandomUtils {

	/**
	 * 返回邮箱验证码
	 * @return
	 */
	@NonNull
	public static String getRandom() {
		String random = "";
		for(int i = 0;i < 6; i++) {
			int j=(int)(10*Math.random());
			random += j;
		}
		return random;
	}
	
	/**
	 * 返回 uuid
	 * @return
	 */
	@NonNull
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("[-]", "");
	}
	
}

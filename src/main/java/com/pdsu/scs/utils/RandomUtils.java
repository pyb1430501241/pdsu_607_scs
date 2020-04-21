package com.pdsu.scs.utils;

/**
 * 随机数生成工具
 * @author Admin
 *
 */
public class RandomUtils {

	public static String getRandom() {
		String random = "";
		for(int i = 0;i < 6; i++) {
			int j=(int)(10*Math.random());
			random += j;
		}
		return random;
	}
	
}

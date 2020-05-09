package com.pdsu.scs.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 格式相关工具类
 * @author 半梦
 *
 */
public class SimpleUtils {

	public static String getSimpleDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}
	
	public static String getSimpleDateSecond() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	
	/**
	 * 返回一段时间的差值
	 * @param startDate 开始的时间
	 * @param endDate   结束的时间
	 * @return
	 */
	public static long getSimpleDateDifference(String startDate, String endDate) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long diff = -1;
		try {
			Date start = dateFormat.parse(startDate);
			Date end = dateFormat.parse(endDate);
			diff = (end.getTime() - start.getTime())/1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return diff;
	}
	
	/**
	 * 拼装字符串
	 * @param args
	 * @return
	 */
	public static String toString(Object... args) {
		String str = "";
		for(Object t : args) {
			str += t.toString();
		}
		return str;
	}
	
	/**
	 * 提供文件名, 返回文件后缀名
	 * @param name
	 * @return
	 */
	public static String getSuffixName(String name) {
		return name.substring(name.lastIndexOf("."), name.length());
	}
}

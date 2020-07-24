package com.pdsu.scs.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.search.SearchHit;

/**
 * 格式化相关工具类
 * @author 半梦
 *
 */
public class SimpleUtils {

	/**
	 * 返回当前时间
	 * @return
	 */
	public static String getSimpleDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}
	
	/**
	 * 返回当前时间, 带时分秒
	 * @return
	 */
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
		List<Object> list =Arrays.asList(args);
		return list.toString();
	}
	
	/**
	 * @param name
	 * @return
	 */
	public static String getSuffixName(String name) {
		return name.substring(name.lastIndexOf("."), name.length());
	}
	
	
	private static final String USERINFORMATION = "EsUserInformation";
	private static final String BLOBINFORMATION = "EsBlobInformation";
	private static final String FILEINFORMATION = "EsFileInformation";
	
	/**
	 * 获取该类的构造器
	 * @param clazz
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	private static Constructor<?> getConstructorByClass(Class<?> clazz) throws NoSuchMethodException, SecurityException{
		String name = getSuffixName(clazz.getName()).replaceFirst("[.]", "").toString();
		Constructor<?> constructor = null;
		switch (name) {
			case USERINFORMATION:
				constructor = clazz.getDeclaredConstructor(Integer.class, Integer.class, String.class, Integer.class, String.class);
				break;
			case BLOBINFORMATION:
				constructor = clazz.getDeclaredConstructor(Integer.class, String.class, String.class);
				break;	
			case FILEINFORMATION:
				constructor = clazz.getDeclaredConstructor(String.class, String.class, Integer.class);
				break;	
			default:
				return null;
		}
		constructor.setAccessible(true);
		return constructor;
	}
	
	/**
	 * 拼装 SearchHit 成对应的实体类
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static List<?> getObjectBySearchHit(SearchHit [] searchHits, Class<?> clazz) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if(getConstructorByClass(clazz) == null) {
			throw new NoSuchMethodException("无此构造方法");
		}
		return getListBySearchHit(searchHits, getConstructorByClass(clazz));
	}
	
	/**
	 * 把数据封装成集合
	 * @param searchHits
	 * @param constructor
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private static List<Object> getListBySearchHit(SearchHit [] searchHits, Constructor<?> constructor) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		List<Object> list = new ArrayList<>();
		for(SearchHit hit : searchHits) {
			Map<String, Object> map = hit.getSourceAsMap();
			list.add(getObjectByMap(map, constructor));
		}
		return list;
	}
	
	/**
	 * 根据构造器把 map 封装成对象
	 * @param map
	 * @param constructor
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private static Object getObjectByMap(Map<String, Object> map, Constructor<?> constructor) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Set<String> keySet = map.keySet();
		Object [] objects = new Object[keySet.size()];
		int i = 0;
		for(String key : keySet) {
			objects[i] = map.get(key);
			i++;
		}
		return constructor.newInstance(objects);
	}
	
	/**
	 * 根据类来拼装该类的对象
	 * @param map
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Object getObjectByMapAndClass(Map<String, Object> map, Class<?> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		if(getConstructorByClass(clazz) == null) {
			throw new NoSuchMethodException("无此构造方法");
		}
		return getObjectByMap(map, getConstructorByClass(clazz));
	}
	
	/**
	 * 邮箱加密
	 * @param str
	 * @return
	 */
	public static String getAsteriskForString(String str) {
		StringBuilder builder = new StringBuilder(str);
		builder.replace(3, 7, "****");
		return builder.toString();
	}
	
}

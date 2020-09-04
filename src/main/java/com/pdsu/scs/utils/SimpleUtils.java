package com.pdsu.scs.utils;

import org.elasticsearch.search.SearchHit;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
	 * 单位: 秒
	 * @param startDate 开始的时间
	 * @param endDate   结束的时间
	 * @return
	 */
	public static long getSimpleDateDifference(@NonNull String startDate, @NonNull String endDate) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long diff = -1;
		try {
			Date start = dateFormat.parse(startDate);
			Date end = dateFormat.parse(endDate);
			diff = (end.getTime() - start.getTime())/1000;
		} catch (ParseException e) {
		}
		return diff;
	}
	
	/**
	 * 拼装字符串
	 * @param args
	 * @return
	 */
	public static String toString(@Nullable Object... args) {
		List<Object> list = Arrays.asList(args);
		return list.toString();
	}
	
	/**
	 * 获取文件后缀名
	 * @param name
	 * @return
	 */
	public static String getSuffixName(@NonNull String name) {
		return name.substring(name.lastIndexOf("."), name.length());
	}

	/**
	 * 获取文件后缀名, 除去点
	 * @param name
	 * @return
	 */
	public static String getSuffixNameExceptPoint(@NonNull String name) {
		return name.substring(name.lastIndexOf(".") + 1, name.length());
	}
	
	/**
	 * 获取该类的构造器
	 * @param clazz
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	private static Constructor<?> getConstructorByClass(Class<?> clazz) throws NoSuchMethodException, SecurityException{
		String name = getSuffixName(clazz.getName()).replaceFirst("[.]", "");
		Constructor<?> constructor = null;
		switch (name) {
			case "EsUserInformation":
				constructor = clazz.getDeclaredConstructor(Integer.class, Integer.class, String.class, Integer.class, String.class);
				break;
			case "EsBlobInformation":
				constructor = clazz.getDeclaredConstructor(Integer.class, String.class, String.class);
				break;	
			case "EsFileInformation":
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
	public static List<?> getObjectBySearchHit(@NonNull SearchHit [] searchHits, @NonNull Class<?> clazz) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
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
	 * 仅支持 EsUserInformation, EsBlobInformation, EsFileInformation
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
	public static Object getObjectByMapAndClass(@NonNull Map<String, Object> map, Class<?> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
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
	public static String getAsteriskForString(@NonNull String str) {
		StringBuilder builder = new StringBuilder(str);
		builder.replace(2, 8, "******");
		return builder.toString();
	}
	
	/**
	 * 1秒
	 */
	public static final long CSC_SECOND = 1;
	
	/**
	 * 一分钟
	 */
	public static final long CSC_MINUTE = CSC_SECOND * 60;
	
	/**
	 * 一小时
	 */
	public static final long CSC_HOURS = CSC_MINUTE * 60;
	
	/**
	 * 一天
	 */
	public static final long CSC_DAY = CSC_HOURS * 24;
	
	/**
	 * 一周
	 */
	public static final long CSC_WEEK = CSC_DAY * 7;
	
	/**
	 * 一月
	 */
	public static final long CSC_MONTH = CSC_DAY * 30;
	
	/**
	 * 一年
	 */
	public static final long CSC_YEAR = CSC_DAY * 365;
	
	private static final Map<Long, String> MAP = new HashMap<Long, String>();
	
	private static final Set<Long> KETSET = new TreeSet<Long>(Comparator.reverseOrder());
	
	static {
		MAP.put(CSC_WEEK, "周前");
		MAP.put(CSC_HOURS, "小时前");
		MAP.put(CSC_MINUTE, "分钟前");
		MAP.put(CSC_DAY, "天前");
		KETSET.addAll(MAP.keySet());
	}
	
	/**
	 * 获取固定形式的时间
	 * @param startDate
	 * 	初始时间
	 * @return
	 * 	当时间差值小于一分钟时返回: 刚刚, 
	 * 	当时间差值大于一月时返回: startDate, 
	 *  当时间差值位于两者之间时返回: xxx前.
	 */
	public static String getSimpleDateDifferenceFormat(@NonNull final String startDate) {
		long t = getSimpleDateDifference(startDate, getSimpleDateSecond());
		StringBuilder builder = new StringBuilder();
		if (t < CSC_MINUTE) {
			builder.append("刚刚");
			return builder.toString();
		}
		if (t >= CSC_MONTH) {
			return startDate;
		}
		for(Long l : KETSET) {
			if (t >= l) {
				builder.append(t / l);
				builder.append(MAP.get(l));
				return builder.toString();
			}
		}
		return startDate;
	}
	
}

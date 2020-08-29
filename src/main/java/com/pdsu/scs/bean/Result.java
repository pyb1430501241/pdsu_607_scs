package com.pdsu.scs.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理AJAX请求的数据
 * @author Admin
 *
 */
public class Result{
	/**
	 * 
	 */
	private Map<String, Object> json = new HashMap<String, Object>();
	
	public Map<String, Object> getJson() {
		return json;
	}

	public void setJson(Map<String, Object> json) {
		this.json = json;
	}

	public static Result success() {
		Result result = new Result();
		result.add("msg", "success");
		result.add("code", 200);
		return result;
	}
	
	public static Result fail() {
		Result result = new Result();
		result.add("code", 404);
		result.add("msg", "fail");
		return result;
	}
	
	public static Result accepted() {
		Result result = new Result();
		result.add("code", 202);
		result.add("msg", "There is no data for the time being.");
		return result;
	}

	public static Result permission() {
		Result result = new Result();
		result.add("code", 403);
		result.add("msg", "Insufficient permissions");
		return result;
	}

	public static Result permissionByTeacher() {
		Result result = new Result();
		result.add("code", 403);
		result.add("msg", "Insufficient permissions, At least permission is required for the teacher.");
		return result;
	}

	public Result add(String key, Object value) {
		this.json.put(key, value);
		return this;
	}
	
	@Override
	public String toString() {
		return "Result [json=" + json + "]";
	}
}

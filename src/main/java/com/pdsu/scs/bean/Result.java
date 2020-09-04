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

	public static Result success() {
		Result result = new Result();
		result.add("msg", "success");
		result.add("code", 200);
		return result;
	}

	public static Result bedRequest() {
		Result result = new Result();
		result.add("msg", "bed request");
		result.add("code", 400);
		return result;
	}

	public static Result fail() {
		Result result = new Result();
		result.add("code", 500);
		result.add("msg", "fail");
		return result;
	}

	public static Result notFound() {
		Result result = new Result();
		result.add("code", 404);
		result.add("msg", "The page you want was not found");
		return result;
	}

	public static Result accepted() {
		Result result = new Result();
		result.add("code", 202);
		result.add("msg", "Ah summer, what power you have to make us suffer and like it.");
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

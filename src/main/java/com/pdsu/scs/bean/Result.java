package com.pdsu.scs.bean;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.impl.util.json.JSONObject;

/**
 * 处理AJAX请求
 * @author Admin
 *
 */
public class Result{
	/*
	 * private Integer code;
	 * 
	 * private Map<String, Object> map = new HashMap<String, Object>();
	 * 
	 * public Integer getCode() { return code; }
	 * 
	 * public void setCode(Integer code) { this.code = code; }
	 * 
	 * public Map<String, Object> getMap() { return map; }
	 * 
	 * public void setMap(Map<String, Object> map) { this.map = map; }
	 * 
	 * public static Result success() { Result result = new Result(); result.code =
	 * 200; return result; }
	 * 
	 * public static Result fail() { Result result = new Result(); result.code =
	 * 404; return result; }
	 * 
	 * public Result add(String key, Object value) { map.put(key, value); return
	 * this; }
	 */
	
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
	
	public Result add(String key, Object value) {
		this.json.put(key, value);
		return this;
	}
}

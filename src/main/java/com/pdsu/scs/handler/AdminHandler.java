/*
 * 权限模型采用 RBAC 模型
 * 总共分为三个等级
 * 1. admin
 * 2. teacher
 * 3. student
 */
package com.pdsu.scs.handler;

import org.springframework.web.bind.annotation.RequestParam;

import com.pdsu.scs.bean.Result;

/**
 * @author 半梦
 */
public class AdminHandler {
	
	
	public Result getUserInformationList(@RequestParam(value = "p", defaultValue = "1") Integer p) {
		System.out.println("测试");
		return Result.fail();
	}
	
}

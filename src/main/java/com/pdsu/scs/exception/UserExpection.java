package com.pdsu.scs.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 用户相关的异常
 * @author 半梦
 *
 */
public class UserExpection extends AuthenticationException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String exceptiopn;

	public String getExceptiopn() {
		return exceptiopn;
	}

	public void setExceptiopn(String exceptiopn) {
		this.exceptiopn = exceptiopn;
	}

	public UserExpection(String exceptiopn) {
		super(exceptiopn);
	}
}

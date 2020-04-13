package com.pdsu.scs.Exception;

import org.apache.shiro.authc.AuthenticationException;

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

package com.pdsu.scs.exception.web.user;

import com.pdsu.scs.exception.web.WebException;

/**
 * 用户相关的异常
 * @author 半梦
 *
 */
public class UserExpection extends WebException{

	private static final long serialVersionUID = 1L;

	public UserExpection(String exceptiopn) {
		super(exceptiopn);
	}
}

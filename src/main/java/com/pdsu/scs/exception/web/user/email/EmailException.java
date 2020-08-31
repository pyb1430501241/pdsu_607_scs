package com.pdsu.scs.exception.web.user.email;

import com.pdsu.scs.exception.web.user.UserExpection;

/**
 * 邮箱相关异常父类
 * @author 半梦
 *
 */
public class EmailException extends UserExpection{

	public EmailException(String exceptiopn) {
		super(exceptiopn);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}

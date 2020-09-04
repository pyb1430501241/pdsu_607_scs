package com.pdsu.scs.exception.web.user.email;

/**
 * 邮箱相关异常父类
 * @author 半梦
 *
 */
public class EmailException extends org.apache.commons.mail.EmailException {

	public EmailException(String exceptiopn) {
		super(exceptiopn);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}

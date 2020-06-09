package com.pdsu.scs.exception;

/**
 * 网页相关的异常
 * @author 半梦
 *
 */
public class WebException extends Exception{

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

	public WebException() {
	}
	
	public WebException(String exception){
		super(exception);
	}
}

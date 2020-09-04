package com.pdsu.scs.exception.web.file;

/**
 * 学号，文件标题同时重复异常
 * @author Admin
 *
 */
public class UidAndTitleRepetitionException extends FileException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public UidAndTitleRepetitionException(String msg) {
		super(msg);
	}
	
}

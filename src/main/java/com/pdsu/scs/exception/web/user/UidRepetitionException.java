package com.pdsu.scs.exception.web.user;

/**
 * 学号重复异常
 * @author Admin
 *
 */
public class UidRepetitionException extends UserExpection{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UidRepetitionException(String msg) {
		super(msg);
	}
	
}

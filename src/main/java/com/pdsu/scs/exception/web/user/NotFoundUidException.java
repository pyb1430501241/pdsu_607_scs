package com.pdsu.scs.exception.web.user;

/**
 * 
 * @author Admin
 *
 */
public class NotFoundUidException extends UserExpection{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public NotFoundUidException(String msg) {
		super(msg);
	}
}

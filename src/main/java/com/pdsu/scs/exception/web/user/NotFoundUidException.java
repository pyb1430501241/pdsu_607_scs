package com.pdsu.scs.exception.web.user;

/**
 * 
 * @author 半梦
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

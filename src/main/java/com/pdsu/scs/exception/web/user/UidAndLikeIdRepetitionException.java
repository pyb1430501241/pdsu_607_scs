package com.pdsu.scs.exception.web.user;

/**
 * 学号及关注人学号重复
 * @author 半梦
 *
 */
public class UidAndLikeIdRepetitionException extends UidRepetitionException{

	public UidAndLikeIdRepetitionException(String msg) {
		super(msg);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}

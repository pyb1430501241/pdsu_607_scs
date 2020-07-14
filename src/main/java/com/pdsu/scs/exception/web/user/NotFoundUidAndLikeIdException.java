package com.pdsu.scs.exception.web.user;

/**
 *  未找到关注信息异常
 * @author 半梦
 *
 */
public class NotFoundUidAndLikeIdException extends NotFoundUidException{

	public NotFoundUidAndLikeIdException(String msg) {
		super(msg);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}

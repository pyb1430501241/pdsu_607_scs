package com.pdsu.scs.exception.web;

import com.pdsu.scs.exception.CodeSharingCommunityException;

/**
 * 网页相关的异常
 * @author 半梦
 *
 */
public class WebException extends CodeSharingCommunityException{
	
	private static final long serialVersionUID = 1L;
	
	public WebException(String exception){
		super(exception);
	}
}

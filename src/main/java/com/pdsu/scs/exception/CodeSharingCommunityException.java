package com.pdsu.scs.exception;

/**
 * 本项目最高级系统异常, 优先级最低
 * 
 * @author 半梦
 *
 */
public class CodeSharingCommunityException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CodeSharingCommunityException(String msg) {
		super(msg);
	}

}

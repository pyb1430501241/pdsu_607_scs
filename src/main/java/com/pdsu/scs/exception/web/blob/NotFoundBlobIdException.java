package com.pdsu.scs.exception.web.blob;

/**
 * 没有找到博客页面的id
 * @author Admin
 *
 */
public class NotFoundBlobIdException extends BlobException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NotFoundBlobIdException(String msg) {
		super(msg);
	}
	
	public NotFoundBlobIdException() {
		this("该文章不存在");
	}

}

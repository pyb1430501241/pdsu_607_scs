package com.pdsu.scs.exception.web.blob;
/**
 * 重复点赞
 * @author 半梦
 */
public class RepetitionThumbsException extends BlobException{

	public RepetitionThumbsException(String exception) {
		super(exception);
	}

	public RepetitionThumbsException() {
		this("你已点赞该文章");
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}

package com.pdsu.scs.exception.web.blob.comment;

/**
 * 未找到评论
 * @author 半梦
 *
 */
public class NotFoundCommentIdException extends CommentException{

	public NotFoundCommentIdException(String exception) {
		super(exception);
	}
	
	public NotFoundCommentIdException() {
		this("该评论不存在");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}

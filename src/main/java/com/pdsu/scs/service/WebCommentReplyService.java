package com.pdsu.scs.service;

import java.util.List;

import com.pdsu.scs.bean.WebComment;
import com.pdsu.scs.bean.WebCommentReply;
import com.pdsu.scs.exception.web.blob.NotFoundBlobIdException;
import com.pdsu.scs.exception.web.blob.comment.NotFoundCommentIdException;

/**
 * 回复评论
 * @author 半梦
 *
 */
public interface WebCommentReplyService {

	/**
	 * 插入
	 * @param webCommentReply
	 * @return
	 */
	public boolean insert(WebCommentReply webCommentReply) throws NotFoundBlobIdException, NotFoundCommentIdException;
	
	/**
	 * 博客是否存在
	 * @param webid
	 * @return
	 */
	public boolean countByWebid(Integer webid);
	
	/**
	 * 评论是否存在
	 * @param cid
	 * @return
	 */
	public boolean countByCid(Integer cid);

	/**
	 * 根据评论查询所有回复
	 * @param commentList
	 * @return
	 */
	public List<WebCommentReply> selectCommentReplysByWebComments(List<WebComment> commentList);

}

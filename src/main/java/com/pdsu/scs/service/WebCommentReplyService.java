package com.pdsu.scs.service;

import java.util.List;

import com.pdsu.scs.bean.WebComment;
import com.pdsu.scs.bean.WebCommentReply;
import com.pdsu.scs.exception.web.blob.NotFoundBlobIdException;
import com.pdsu.scs.exception.web.blob.comment.NotFoundCommentIdException;
import org.springframework.lang.NonNull;

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
	public boolean insert(@NonNull WebCommentReply webCommentReply) throws NotFoundBlobIdException, NotFoundCommentIdException;
	
	/**
	 * 博客是否存在
	 * @param webid
	 * @return
	 */
	public boolean countByWebid(@NonNull Integer webid);
	
	/**
	 * 评论是否存在
	 * @param cid
	 * @return
	 */
	public boolean countByCid(@NonNull Integer cid);

	/**
	 * 根据评论查询所有回复
	 * @param commentList
	 * @return
	 */
	public List<WebCommentReply> selectCommentReplysByWebComments(@NonNull List<WebComment> commentList);
	
	/**
	 * 根据网页ID查询所有评论回复
	 * @param webid
	 * @return
	 */
	public List<WebCommentReply> selectCommentReplysByWebId(@NonNull Integer webid) throws NotFoundBlobIdException;

	/**
	 * 根据用户所有文章和uid获取总共有多少评论回复
	 * @param webs
	 * @param uid
	 * @return
	 */
	public Integer countByWebsAndUid(@NonNull List<Integer> webs);

}

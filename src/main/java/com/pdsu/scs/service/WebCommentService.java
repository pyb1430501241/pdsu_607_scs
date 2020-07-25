package com.pdsu.scs.service;

import java.util.List;

import com.pdsu.scs.bean.WebComment;
import com.pdsu.scs.exception.web.blob.NotFoundBlobIdException;

/**
 * 评论
 * @author 半梦
 *
 */
public interface WebCommentService {

	/**
	 * 插入评论
	 * @param webComment
	 * @return
	 */
	public boolean insert(WebComment webComment) throws NotFoundBlobIdException;
	
	/**
	 * 查询博客是否存在
	 * @param webid
	 * @return
	 */
	public boolean countByWebid(Integer webid);

	/**
	 * 根据 webid 获取文章评论
	 * @param id
	 * @return
	 */
	public List<WebComment> selectCommentsByWebId(Integer webid) throws NotFoundBlobIdException;

	/**
	 * 根据学号获取一个人的总评论数
	 * @param uid
	 * @return
	 */
	public Integer countByUid(Integer uid);
	
}

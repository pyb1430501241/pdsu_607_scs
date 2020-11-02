package com.pdsu.scs.service;

import com.pdsu.scs.bean.WebComment;
import com.pdsu.scs.exception.web.blob.NotFoundBlobIdException;
import org.springframework.lang.NonNull;

import java.util.List;

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
	public boolean insert(@NonNull WebComment webComment) throws NotFoundBlobIdException;
	
	/**
	 * 查询博客是否存在
	 * @param webid
	 * @return
	 */
	public boolean countByWebid(@NonNull Integer webid);

	/**
	 * 根据 webid 获取文章评论
	 * @param id
	 * @return
	 */
	public List<WebComment> selectCommentsByWebId(@NonNull Integer webid) throws NotFoundBlobIdException;

	/**
	 * 根据学号获取一个人的总评论数
	 * @param uid
	 * @return
	 */
	public Integer countByUid(@NonNull Integer uid);

	/**
	 *  获取评论
	 * @param cid
	 * @return
	 */
    public WebComment selectCommentById(Integer cid);
}

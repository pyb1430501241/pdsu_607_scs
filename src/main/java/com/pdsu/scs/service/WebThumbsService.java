package com.pdsu.scs.service;

import java.util.List;

import com.pdsu.scs.bean.WebThumbs;
import com.pdsu.scs.exception.web.blob.NotFoundBlobIdException;
import com.pdsu.scs.exception.web.blob.RepetitionThumbsException;
import org.springframework.lang.NonNull;

/**
 * 该接口负责提供点赞相关的方法
 * @author 半梦
 *
 */
public interface WebThumbsService {
	
	/**
	 * 根据网页ID集合获取这些文章的点赞数
	 */
	public List<Integer> selectThumbssForWebId(@NonNull List<Integer> webids);

	/**
	 * 根据网页ID获取文章的点赞数
	 */
	public Integer selectThumbsForWebId(@NonNull Integer webid);

	/**
	 * 根据学号获取一个人的总点赞数
	 */
	public Integer countThumbsByUid(@NonNull Integer uid);

	/**
	 * 插入一条点赞记录
	 * @param webThumbs
	 * @return
	 */
	public boolean insert(@NonNull WebThumbs webThumbs) throws NotFoundBlobIdException, RepetitionThumbsException;
	
	/**
	 * 查询网页是否存在
	 */
	public boolean countByWebId(@NonNull Integer webid) ;

	/**
	 * 删除一条点赞记录
	 * @param webid
	 * @param uid
	 * @return
	 */
	public boolean deletebyWebIdAndUid(@NonNull Integer webid, @NonNull Integer uid) throws RepetitionThumbsException;

	/**
	 * 查询用户是否点赞
	 * @param webid
	 * @param uid
	 * @return
	 */
	public boolean countByWebIdAndUid(@NonNull Integer webid, @NonNull Integer uid);
}

package com.pdsu.scs.service;

import java.util.List;

import com.pdsu.scs.bean.MyCollection;
import com.pdsu.scs.exception.web.blob.NotFoundBlobIdException;
import com.pdsu.scs.exception.web.user.UidAndWebIdRepetitionException;
import org.springframework.lang.NonNull;

/**
 * 该接口提供和收藏相关的方法
 * @author 半梦
 *
 */
public interface MyCollectionService {
	
	/**
	 * 插入一个收藏记录
	 * @param con
	 * @return
	 */
	public boolean insert(@NonNull MyCollection con) throws UidAndWebIdRepetitionException;
	
	/**
	 * 根据网页id获取此网页的收藏量
	 * @param webid
	 * @return
	 */
	public Integer selectCollectionsByWebId(@NonNull Integer webid) throws NotFoundBlobIdException;

	/**
	 * 根据一组网页id获取集群
	 * @param webids
	 * @return
	 */
	public List<Integer> selectCollectionssByWebIds(@NonNull List<Integer> webids);

	/**
	 * 删除一个收藏记录
	 * @param uid
	 * @param webid
	 * @return
	 */
	public boolean delete(@NonNull Integer uid, @NonNull Integer webid) throws UidAndWebIdRepetitionException;
	
	/**
	 * 查询指定收藏记录是否存在
	 * @param uid
	 * @param webid
	 * @return
	 */
	public boolean countByUidAndWebId(@NonNull Integer uid, @NonNull Integer webid);

	/**
	 * 查询文章是否存在
	 * @param webid
	 * @return
	 */
	public boolean countByWebId(@NonNull Integer webid);

	/**
	 * 获取一个人的总收藏量
	 * @param uid
	 * @return
	 */
	public Integer countCollectionByUid(@NonNull Integer uid);

	/**
	 * 获取用户收藏的文章id
	 * @param uid
	 * @return
	 */
	public List<MyCollection> selectWebIdsByUid(@NonNull Integer uid);
	
}

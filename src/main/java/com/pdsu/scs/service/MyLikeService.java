package com.pdsu.scs.service;

import java.util.List;

import com.pdsu.scs.bean.MyLike;
import com.pdsu.scs.exception.web.user.NotFoundUidAndLikeIdException;
import com.pdsu.scs.exception.web.user.NotFoundUidException;
import com.pdsu.scs.exception.web.user.UidAndLikeIdRepetitionException;
import org.springframework.lang.NonNull;

/**
 * 该接口提供关注相关的方法
 * @author 半梦
 *
 */
public interface MyLikeService {

	/**
	 * 根据学号查询自己的关注个数
	 */
	public long countByUid(@NonNull Integer uid) throws NotFoundUidException;

	/**
	 * 根据学号查询自己的粉丝个数
	 */
	public long countByLikeId(@NonNull Integer likeId) throws NotFoundUidException;

	/**
	 *根据学号查询自己的粉丝学号
	 * @param likeId
	 * @return
	 * @throws NotFoundUidException
	 */
	public List<Integer> selectUidByLikeId(@NonNull Integer likeId)throws NotFoundUidException;

	/**
	 * 插入
	 */
	public boolean insert(@NonNull MyLike myLike) throws UidAndLikeIdRepetitionException;

	/**
	 * 根据学号查询自己关注人的学号
	 */
	public List<Integer> selectLikeIdByUid(@NonNull Integer uid) throws NotFoundUidException;

	/**
	 * 判断用户是否存在
	 * @param uid
	 * @return
	 */
	public boolean isByUid(@NonNull Integer uid);

	/**
	 * 根据 uid likeid 判断是否已关注
	 * @param uid
	 * @param likeId
	 * @return
	 */
	public boolean countByUidAndLikeId(@NonNull Integer uid, @NonNull Integer likeId);

	/**
	 * 根据 uid likeid 删除关注记录
	 * @param likeId
	 * @param uid
	 * @return
	 * @throws NotFoundUidAndLikeIdException
	 */
	public boolean deleteByLikeIdAndUid(@NonNull Integer likeId, @NonNull Integer uid) throws NotFoundUidAndLikeIdException;

	/**
	 * 获取自己是否关注这些人
	 * 待优化, 目前算法很缓慢
	 * @param uid
	 * @param uids
	 * @return
	 */
	public List<Boolean> countByUidAndLikeId(@NonNull Integer uid, @NonNull List<Integer> uids) throws NotFoundUidException;
}
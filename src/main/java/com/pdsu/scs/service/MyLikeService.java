package com.pdsu.scs.service;

import java.util.List;

import com.pdsu.scs.bean.MyLike;
import com.pdsu.scs.exception.web.user.NotFoundUidAndLikeIdException;
import com.pdsu.scs.exception.web.user.NotFoundUidException;
import com.pdsu.scs.exception.web.user.UidAndLikeIdRepetitionException;

/**
 * 该接口提供关注相关的方法
 * @author 半梦
 *
 */
public interface MyLikeService {

	//根据学号查询自己的关注个数
	public long countByUid(Integer uid) throws NotFoundUidException;
	
	//根据学号查询自己的粉丝个数
	public long countByLikeId(Integer likeId) throws NotFoundUidException;
	
	//插入
	public boolean insert(MyLike myLike) throws UidAndLikeIdRepetitionException;
	
	//根据学号查询自己关注人的学号
	public List<Integer> selectLikeIdByUid(Integer uid) throws NotFoundUidException;
	
	//根据学号查询自己的粉丝学号
	public List<Integer> selectUidByLikeId(Integer likeId)throws NotFoundUidException;
	
	//判断用户是否存在
	public boolean isByUid(Integer uid);
	
	//根据 uid likeid 判断是否已关注
	public boolean countByUidAndLikeId(Integer uid, Integer likeId);

	//根据 uid likeid 删除关注记录
	public boolean deleteByLikeIdAndUid(Integer likeId, Integer uid) throws NotFoundUidAndLikeIdException;

	/**
	 * 获取自己是否关注这些人
	 * 待优化, 目前算法很缓慢
	 * @param uid
	 * @param uids
	 * @return
	 */
	public List<Boolean> countByUidAndLikeId(Integer uid, List<Integer> uids) throws NotFoundUidException;
}
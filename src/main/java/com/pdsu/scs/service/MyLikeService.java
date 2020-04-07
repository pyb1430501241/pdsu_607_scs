package com.pdsu.scs.service;

import java.util.List;

import com.pdsu.scs.bean.MyLike;

public interface MyLikeService {

	//根据学号查询自己的关注个数
	public long countByUid(Integer uid);
	
	//根据学号查询自己的粉丝个数
	public long countByLikeId(Integer likeId);
	
	//插入
	public boolean insert(MyLike myLike);
	
	//根据学号查询自己关注人的学号
	public List<Integer> selectLikeIdByUid(Integer uid);
	
	//根据学号查询自己的粉丝学号
	public List<Integer> selectUidByLikeId(Integer likeId);
	
}
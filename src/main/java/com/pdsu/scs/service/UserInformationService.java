package com.pdsu.scs.service;

import java.util.List;

import com.pdsu.scs.bean.UserInformation;

public interface UserInformationService {
	
	//增加用户
	public boolean inset(UserInformation information);
	
	//根据学号删除用户
	public boolean deleteByUid(Integer uid);
	
	//根据学号查询用户
	public UserInformation selectByUid(Integer uid);
	
	//根据学号查询其关注人的信息
	public List<UserInformation> selectUsersByUid(Integer uid);
	
	//根据学号查询其粉丝信息
	public List<UserInformation> selectUsersByLikeId(Integer likeId);

	public List<UserInformation> selectUsersByUids(List<Integer> uids);
	
}

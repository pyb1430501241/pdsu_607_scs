package com.pdsu.scs.service;

import java.util.List;

import com.pdsu.scs.bean.UserInformation;

/**
 * 与用户信息相关的方法
 * @author Admin
 *
 */
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

	//根据一组学号获取一组学生信息
	public List<UserInformation> selectUsersByUids(List<Integer> uids);

	//查询是否有此账号
	public long countByUid(Integer uid);
	
	//修改密码
	public boolean ModifyThePassword(Integer uid, String password);
	
}

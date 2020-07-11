package com.pdsu.scs.service;

import java.util.List;

import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.exception.web.DeleteInforException;
import com.pdsu.scs.exception.web.es.InsertException;
import com.pdsu.scs.exception.web.user.NotFoundUidException;
import com.pdsu.scs.exception.web.user.UidRepetitionException;

/**
 * 与用户信息相关的方法
 * @author 半梦
 *
 */
public interface UserInformationService {
	
	//增加用户
	public boolean inset(UserInformation information) throws UidRepetitionException, InsertException;
	
	//根据学号删除用户
	public boolean deleteByUid(Integer uid) throws NotFoundUidException, DeleteInforException;
	
	//根据学号查询用户
	public UserInformation selectByUid(Integer uid);
	
	//根据学号查询其关注人的信息
	public List<UserInformation> selectUsersByUid(Integer uid) throws NotFoundUidException;
	
	//根据学号查询其粉丝信息
	public List<UserInformation> selectUsersByLikeId(Integer likeId) throws NotFoundUidException;

	//根据一组学号获取一组学生信息
	public List<UserInformation> selectUsersByUids(List<Integer> uids);

	//查询是否有此账号
	public int countByUid(Integer uid);
	
	//修改密码
	public boolean ModifyThePassword(Integer uid, String password);

	public int countByUserName(String username);
	
}

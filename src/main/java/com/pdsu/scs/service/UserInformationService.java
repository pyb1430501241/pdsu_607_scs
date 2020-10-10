package com.pdsu.scs.service;

import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.exception.web.DeleteInforException;
import com.pdsu.scs.exception.web.es.InsertException;
import com.pdsu.scs.exception.web.user.NotFoundUidException;
import com.pdsu.scs.exception.web.user.UidRepetitionException;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * 与用户信息相关的方法
 * @author 半梦
 *
 */
public interface UserInformationService {

	/**
	 * 增加用户
	 * @param information
	 * @return
	 * @throws UidRepetitionException
	 * @throws InsertException
	 */
	public boolean insert(@NonNull UserInformation information) throws UidRepetitionException;

	/**
	 * 根据学号删除用户
	 * @param uid
	 * @return
	 * @throws NotFoundUidException
	 * @throws DeleteInforException
	 */
	public boolean deleteByUid(@NonNull Integer uid) throws NotFoundUidException, DeleteInforException;

	/**
	 *
	 * 根据学号查询用户
	 * @param uid
	 * @return
	 */
	public UserInformation selectByUid(@NonNull Integer uid);

	/**
	 * 根据学号查询其关注人的信息
	 * @param uid
	 * @return
	 * @throws NotFoundUidException
	 */
	public List<UserInformation> selectUsersByUid(@NonNull Integer uid) throws NotFoundUidException;

	/**
	 * 根据学号查询其粉丝信息
	 * @param likeId
	 * @return
	 * @throws NotFoundUidException
	 */
	public List<UserInformation> selectUsersByLikeId(@NonNull Integer likeId) throws NotFoundUidException;

	/**
	 * 根据一组学号获取一组学生信息
	 * @param uids
	 * @return
	 */
	public List<UserInformation> selectUsersByUids(@NonNull List<Integer> uids);

	/**
	 * 查询是否有此账号
	 * @param uid
	 * @return
	 */
	public int countByUid(@NonNull Integer uid);

	/**
	 * 修改密码
	 */
	public boolean modifyThePassword(@NonNull Integer uid, @NonNull String password);

	/**
	 * 查询用户名是否存在
	 */
	public int countByUserName(@NonNull String username);

	/**
	 * 更换用户名
	 */
	public boolean updateUserInformation(@NonNull Integer uid, @NonNull UserInformation user) throws NotFoundUidException;

	/**
	 * 获取所有用户
	 * @return
	 */
    public List<UserInformation> selectUserInformations();
}

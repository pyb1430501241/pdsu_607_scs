package com.pdsu.scs.service;

import com.pdsu.scs.bean.MyEmail;
import com.pdsu.scs.exception.web.user.NotFoundUidException;
import com.pdsu.scs.exception.web.user.email.EmailRepetitionException;
import com.pdsu.scs.exception.web.user.email.NotFoundEmailException;

/**
 * 该接口提供和邮箱相关的方法
 * @author 半梦
 *
 */
public interface MyEmailService {

	/**
	 * 查询邮箱是否存在
	 * 返回值为0时代表邮箱不存在
	 * @param email
	 * @return
	 */
	public boolean countByEmail(String email);

	/**
	 * 根据邮箱地址获取一个 MyEamil 的对象
	 * @param email
	 * @return
	 */
	public MyEmail selectMyEmailByEmail(String email) throws NotFoundEmailException;

	/**
	 * 根据学号来获取一个 MyEmail 的对象
	 * @param uid
	 * @return
	 */
	public MyEmail selectMyEmailByUid(Integer uid);

	/**
	 * 插入
	 * @param myEmail
	 * @return
	 * @throws EmailRepetitionException
	 * @throws NotFoundUidException
	 */
	public boolean insert(MyEmail myEmail) throws EmailRepetitionException, NotFoundUidException;
	
	/**
	 * 查询用户是否存在
	 * @param uid
	 * @return
	 */
	public boolean countByUid(Integer uid);

}

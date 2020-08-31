package com.pdsu.scs.service;

import com.pdsu.scs.bean.MyEmail;
import com.pdsu.scs.exception.web.user.NotFoundUidException;
import com.pdsu.scs.exception.web.user.email.EmailRepetitionException;
import com.pdsu.scs.exception.web.user.email.NotFoundEmailException;
import org.springframework.lang.NonNull;

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
	public boolean countByEmail(@NonNull String email);

	/**
	 * 根据邮箱地址获取一个 MyEamil 的对象
	 * @param email
	 * @return
	 */
	public MyEmail selectMyEmailByEmail(@NonNull String email) throws NotFoundEmailException;

	/**
	 * 根据学号来获取一个 MyEmail 的对象
	 * @param uid
	 * @return
	 */
	public MyEmail selectMyEmailByUid(@NonNull Integer uid);

	/**
	 * 插入
	 * @param myEmail
	 * @return
	 * @throws EmailRepetitionException
	 * @throws NotFoundUidException
	 */
	public boolean insert(@NonNull MyEmail myEmail) throws EmailRepetitionException, NotFoundUidException;
	
	/**
	 * 查询用户是否存在
	 * @param uid
	 * @return
	 */
	public boolean countByUid(@NonNull Integer uid);

}

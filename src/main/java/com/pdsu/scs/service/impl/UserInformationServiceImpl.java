package com.pdsu.scs.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.bean.UserInformationExample;
import com.pdsu.scs.bean.UserInformationExample.Criteria;
import com.pdsu.scs.dao.MyLikeMapper;
import com.pdsu.scs.dao.UserInformationMapper;
import com.pdsu.scs.exception.web.user.NotFoundUidException;
import com.pdsu.scs.exception.web.user.UidRepetitionException;
import com.pdsu.scs.service.UserInformationService;
import com.pdsu.scs.utils.HashUtils;

/**
 * 该类继承 UserInformationService 接口, 用于处理与用户有关的逻辑
 * @author 半梦
 *
 */
@Service("userInformationService")
public class UserInformationServiceImpl implements UserInformationService {
	
	@Autowired
	private UserInformationMapper userInformationMapper;
	
	@Autowired
	private MyLikeMapper myLikeMapper;
	
	/**
	 * 插入一个用户信息
	 * @throws UidRepetitionException 
	 */
	@Override
	public boolean inset(UserInformation information) throws UidRepetitionException {
		Integer uid = information.getUid();
		if(countByUid(uid) != 0) {
			throw new UidRepetitionException("学号重复");
		}
		String password = information.getPassword();
		password = HashUtils.getPasswordHash(uid, password);
		information.setPassword(password);
		if(userInformationMapper.insertSelective(information) > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 删除一个用户信息
	 * @throws NotFoundUidException 
	 */
	@Override
	public boolean deleteByUid(Integer uid) throws NotFoundUidException {
		if(countByUid(uid) == 0) {
			throw new NotFoundUidException("该用户不存在");
		}
		UserInformationExample example = new UserInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		if(userInformationMapper.deleteByExample(example) > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 根据用户的uid查询用户信息
	 */
	@Override
	public UserInformation selectByUid(Integer uid) {
		UserInformationExample example = new UserInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return userInformationMapper.selectByExample(example).get(0);
	}
	
	/**
	 * 根据一个用户的uid查询其所有关注人的信息
	 * @throws NotFoundUidException 
	 */
	@Override
	public List<UserInformation> selectUsersByUid(Integer uid) throws NotFoundUidException {
		if(countByUid(uid) == 0) {
			throw new NotFoundUidException("该用户不存在");
		}
		List<Integer> likeids = myLikeMapper.selectLikeIdByUid(uid);
		if(likeids.size() == 0) {
			return null;
		}
		UserInformationExample example = new UserInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidIn(likeids);
		return userInformationMapper.selectByExample(example);
	}
	
	/**
	 * 根据一个用户的 uid 查询一个用户所有粉丝的信息
	 * @throws NotFoundUidException 
	 */
	@Override
	public List<UserInformation> selectUsersByLikeId(Integer likeId) throws NotFoundUidException {
		if(countByUid(likeId) == 0) {
			throw new NotFoundUidException("该用户不存在");
		}
		List<Integer> uids = myLikeMapper.selectLikeIdByUid(likeId);
		if(uids.size() == 0) {
			return null;
		}
		UserInformationExample example = new UserInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidIn(uids);
		return userInformationMapper.selectByExample(example);
	}

	/**
	 * 根据用户的uid集群查询用户信息集群
	 */
	@Override
	public List<UserInformation> selectUsersByUids(List<Integer> uids) {
		List<Integer> f = new ArrayList<Integer>();
		for(Integer uid : uids) {
			if(countByUid(uid) == 0) {
				f.add(uid);
			}
		}
		uids.removeAll(f);
		return userInformationMapper.selectUsersByUids(uids);
	}
	
	/**
	 * 查询是否有此账号
	 */
	@Override
	public int countByUid(Integer uid) {
		UserInformationExample example = new UserInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return (int) userInformationMapper.countByExample(example);
	}
	
	/**
	 * 修改密码
	 */
	@Override
	public boolean ModifyThePassword(Integer uid, String password) {
		UserInformation user = selectByUid(uid);
		user.setPassword(HashUtils.getPasswordHash(uid, password));
		UserInformationExample example = new UserInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		int updateByExample = userInformationMapper.updateByExample(user, example);
		return updateByExample == 0 ? false : true;
	}
}

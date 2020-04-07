package com.pdsu.scs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.bean.UserInformationExample;
import com.pdsu.scs.bean.UserInformationExample.Criteria;
import com.pdsu.scs.dao.UserInformationMapper;
import com.pdsu.scs.service.MyLikeService;
import com.pdsu.scs.service.UserInformationService;
import com.pdsu.scs.utils.HashUtils;

/**
 * 该类继承 UserInformationService 接口, 用于处理与用户有关的逻辑
 * @author Admin
 *
 */
@Service("userInformationService")
public class UserInformationServiceImpl implements UserInformationService {

	@Autowired
	private UserInformationMapper userInformationMapper;
	
	@Autowired
	private MyLikeService myLikeService;
	
	@Override
	public boolean inset(UserInformation information) {
		Integer uid = information.getUid();
		String password = information.getPassword();
		password = HashUtils.getPasswordHash(uid, password);
		information.setPassword(password);
		if(userInformationMapper.insertSelective(information) != 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteByUid(Integer uid) {
		UserInformationExample example = new UserInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		if(userInformationMapper.deleteByExample(example) != 0) {
			return true;
		}
		return false;
	}

	@Override
	public UserInformation selectByUid(Integer uid) {
		UserInformationExample example = new UserInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return userInformationMapper.selectByExample(example).get(0);
	}

	@Override
	public List<UserInformation> selectUsersByUid(Integer uid) {
		List<Integer> likeids = myLikeService.selectLikeIdByUid(uid);
		if(likeids.size() == 0) {
			return null;
		}
		UserInformationExample example = new UserInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidIn(likeids);
		return userInformationMapper.selectByExample(example);
	}

	@Override
	public List<UserInformation> selectUsersByLikeId(Integer likeId) {
		List<Integer> uids = myLikeService.selectLikeIdByUid(likeId);
		if(uids.size() == 0) {
			return null;
		}
		UserInformationExample example = new UserInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidIn(uids);
		return userInformationMapper.selectByExample(example);
	}
}

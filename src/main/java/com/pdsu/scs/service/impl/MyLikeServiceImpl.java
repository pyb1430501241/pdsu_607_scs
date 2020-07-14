package com.pdsu.scs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.MyLike;
import com.pdsu.scs.bean.MyLikeExample;
import com.pdsu.scs.bean.UserInformationExample;
import com.pdsu.scs.bean.MyLikeExample.Criteria;
import com.pdsu.scs.dao.MyLikeMapper;
import com.pdsu.scs.dao.UserInformationMapper;
import com.pdsu.scs.exception.web.user.NotFoundUidAndLikeIdException;
import com.pdsu.scs.exception.web.user.NotFoundUidException;
import com.pdsu.scs.exception.web.user.UidAndLikeIdRepetitionException;
import com.pdsu.scs.service.MyLikeService;

/**
 * 
 * @author 半梦
 *
 */
@Service("myLikeService")
public class MyLikeServiceImpl implements MyLikeService{

	@Autowired
	private MyLikeMapper myLikeMapper;
	
	@Autowired
	private UserInformationMapper userInformationMapper;
	
	@Override
	public long countByUid(Integer uid) throws NotFoundUidException {
		if(!isByUid(uid)) {
			throw new NotFoundUidException("该用户不存在");
		}
		MyLikeExample example = new MyLikeExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return myLikeMapper.countByExample(example);
	}

	@Override
	public long countByLikeId(Integer likeId) throws NotFoundUidException {
		if(!isByUid(likeId)) {
			throw new NotFoundUidException("该用户不存在");
		}
		MyLikeExample example = new MyLikeExample();
		Criteria criteria = example.createCriteria();
		criteria.andLikeIdEqualTo(likeId);
		return myLikeMapper.countByExample(example);
	}

	@Override
	public boolean insert(MyLike myLike) throws UidAndLikeIdRepetitionException {
		if(countByUidAndLikeId(myLike.getUid(), myLike.getLikeId())) {
			throw new UidAndLikeIdRepetitionException("你已关注此用户, 无法重复关注");
		}
		if(myLikeMapper.insertSelective(myLike) > 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public List<Integer> selectLikeIdByUid(Integer uid) throws NotFoundUidException {
		if(!isByUid(uid)) {
			throw new NotFoundUidException("该用户不存在");
		}
		return myLikeMapper.selectLikeIdByUid(uid);
	}
	
	@Override
	public List<Integer> selectUidByLikeId(Integer likeId) throws NotFoundUidException {
		if(!isByUid(likeId)) {
			throw new NotFoundUidException("该用户不存在");
		}
		return myLikeMapper.selectUidByLikeId(likeId);
	}

	@Override
	public boolean isByUid(Integer uid) {
		UserInformationExample example = new UserInformationExample();
		com.pdsu.scs.bean.UserInformationExample.Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return userInformationMapper.countByExample(example) > 0 ? true : false;
	}

	@Override
	public boolean countByUidAndLikeId(Integer uid, Integer likeId) {
		MyLikeExample example = new MyLikeExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		criteria.andLikeIdEqualTo(likeId);
		return myLikeMapper.countByExample(example) > 0 ? true : false;
	}
	
	@Override
	public boolean deleteByLikeIdAndUid(Integer likeId, Integer uid) throws NotFoundUidAndLikeIdException {
		if(!countByUidAndLikeId(uid, likeId)) {
			throw new NotFoundUidAndLikeIdException("您并未关注该用户");
		}
		MyLikeExample example = new MyLikeExample();
		Criteria criteria = example.createCriteria();
		criteria.andLikeIdEqualTo(uid);
		criteria.andUidEqualTo(likeId);
		return myLikeMapper.deleteByExample(example) > 0 ? true : false;
	}
}

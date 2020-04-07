package com.pdsu.scs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.MyLike;
import com.pdsu.scs.bean.MyLikeExample;
import com.pdsu.scs.bean.MyLikeExample.Criteria;
import com.pdsu.scs.dao.MyLikeMapper;
import com.pdsu.scs.service.MyLikeService;

@Service("myLikeService")
public class MyLikeServiceImpl implements MyLikeService{

	@Autowired
	private MyLikeMapper myLikeMapper;
	
	@Override
	public long countByUid(Integer uid) {
		MyLikeExample example = new MyLikeExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return myLikeMapper.countByExample(example);
	}

	@Override
	public long countByLikeId(Integer likeId) {
		MyLikeExample example = new MyLikeExample();
		Criteria criteria = example.createCriteria();
		criteria.andLikeIdEqualTo(likeId);
		return myLikeMapper.countByExample(example);
	}

	@Override
	public boolean insert(MyLike myLike) {
		if(myLikeMapper.insertSelective(myLike) != 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public List<Integer> selectLikeIdByUid(Integer uid) {
		return myLikeMapper.selectLikeIdByUid(uid);
	}
	
	@Override
	public List<Integer> selectUidByLikeId(Integer likeId) {
		return myLikeMapper.selectUidByLikeId(likeId);
	}
}

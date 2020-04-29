package com.pdsu.scs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.MyEmail;
import com.pdsu.scs.bean.MyEmailExample;
import com.pdsu.scs.bean.MyEmailExample.Criteria;
import com.pdsu.scs.dao.MyEmailMapper;
import com.pdsu.scs.service.MyEmailService;

/**
 * 
 * @author 半梦
 *
 */
@Service("myEmailService")
public class MyEmailServiceImpl implements MyEmailService{

	@Autowired
	private MyEmailMapper myEmailMapper;
	
	@Override
	public int countByEmail(String email) {
		MyEmailExample example = new MyEmailExample();
		Criteria criteria = example.createCriteria();
		criteria.andEmailEqualTo(email);
		return (int) myEmailMapper.countByExample(example);
	}

	@Override
	public MyEmail selectMyEmailByEmail(String email) {
		MyEmailExample example = new MyEmailExample();
		Criteria criteria = example.createCriteria();
		criteria.andEmailEqualTo(email);
		return myEmailMapper.selectByExample(example).get(0);
	}

	@Override
	public MyEmail selectMyEmailByUid(Integer uid) {
		MyEmailExample example = new MyEmailExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return myEmailMapper.selectByExample(example).get(0);
	}

}

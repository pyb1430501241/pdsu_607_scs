package com.pdsu.scs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.MyEmail;
import com.pdsu.scs.bean.MyEmailExample;
import com.pdsu.scs.bean.MyEmailExample.Criteria;
import com.pdsu.scs.bean.UserInformationExample;
import com.pdsu.scs.dao.MyEmailMapper;
import com.pdsu.scs.dao.UserInformationMapper;
import com.pdsu.scs.exception.web.user.NotFoundUidException;
import com.pdsu.scs.exception.web.user.email.EmailRepetitionException;
import com.pdsu.scs.exception.web.user.email.NotFoundEmailException;
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
	
	@Autowired
	private UserInformationMapper userInformationMapper;
	
	@Override
	public boolean countByEmail(String email) {
		MyEmailExample example = new MyEmailExample();
		Criteria criteria = example.createCriteria();
		criteria.andEmailEqualTo(email);
		return myEmailMapper.countByExample(example) == 0 ? false : true;
	}

	@Override
	public MyEmail selectMyEmailByEmail(String email) throws NotFoundEmailException {
		if(!countByEmail(email)) {
			throw new NotFoundEmailException("邮箱不存在");
		}
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
		List<MyEmail> list = myEmailMapper.selectByExample(example);
		return list.size() == 0 ? null : list.get(0);
	}

	@Override
	public boolean insert(MyEmail myEmail) throws EmailRepetitionException, NotFoundUidException  {
		if(!countByUid(myEmail.getUid())) {
			throw new NotFoundUidException("该用户不存在");
		}
		if(countByEmail(myEmail.getEmail())) {
			throw new EmailRepetitionException("该邮箱已被绑定");
		}
		return myEmailMapper.insertSelective(myEmail) == 0 ? false : true;
	}

	@Override
	public boolean countByUid(Integer uid) {
		UserInformationExample example = new UserInformationExample();
		com.pdsu.scs.bean.UserInformationExample.Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return userInformationMapper.countByExample(example) == 0 ? false : true;
	}
}

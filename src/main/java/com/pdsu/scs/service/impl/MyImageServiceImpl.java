package com.pdsu.scs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.MyImage;
import com.pdsu.scs.bean.MyImageExample;
import com.pdsu.scs.bean.MyImageExample.Criteria;
import com.pdsu.scs.dao.MyImageMapper;
import com.pdsu.scs.service.MyImageService;

@Service("myImageService")
public class MyImageServiceImpl implements MyImageService {

	@Autowired
	private MyImageMapper myImageMapper;
	
	@Override
	public List<MyImage> selectImagePathByUids(List<Integer> uids) {
		MyImageExample example = new MyImageExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidIn(uids);
		List<MyImage> list = myImageMapper.selectByExample(example);
		return list;
	}

	@Override
	public MyImage selectImagePathByUid(Integer uid) {
		MyImageExample example = new MyImageExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return myImageMapper.selectByExample(example).size() == 0 ? new MyImage(uid, "01.png") : myImageMapper.selectByExample(example).get(0);
	}
}

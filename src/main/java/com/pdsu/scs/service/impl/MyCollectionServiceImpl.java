package com.pdsu.scs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.MyCollection;
import com.pdsu.scs.bean.MyCollectionExample;
import com.pdsu.scs.bean.MyCollectionExample.Criteria;
import com.pdsu.scs.dao.MyCollectionMapper;
import com.pdsu.scs.service.MyCollectionService;

@Service("myCollectionService")
public class MyCollectionServiceImpl implements MyCollectionService{

	@Autowired
	private MyCollectionMapper myCollectionMapper;

	@Override
	public boolean insert(MyCollection con) {
		if(myCollectionMapper.insertSelective(con) != 0) {
			return true;
		}
		return false;
	}

	@Override
	public Integer selectCollectionsByWebId(Integer webid) {
		MyCollectionExample example = new MyCollectionExample();
		Criteria criteria = example.createCriteria();
		criteria.andWidEqualTo(webid);
		return (int) myCollectionMapper.countByExample(example);
	}

	@Override
	public List<Integer> selectCollectionssByWebIds(List<Integer> webids) {
		List<Integer> collectionss = new ArrayList<Integer>();
		for(Integer webid : webids) {
			collectionss.add(selectCollectionsByWebId(webid));
		}
		return collectionss;
	}
	

}

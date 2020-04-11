package com.pdsu.scs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.WebThumbsExample;
import com.pdsu.scs.bean.WebThumbsExample.Criteria;
import com.pdsu.scs.dao.WebThumbsMapper;
import com.pdsu.scs.service.WebThumbsService;

@Service("webThumbsService")
public class WebThumbsServiceImpl implements WebThumbsService {

	@Autowired
	private WebThumbsMapper webThumbsMapper;
	
	@Override
	public List<Integer> selectThumbssForWebId(List<Integer> webids) {
		List<Integer> thumbss = new ArrayList<Integer>();
		for(Integer webid : webids) {
			WebThumbsExample example = new WebThumbsExample();
			Criteria criteria = example.createCriteria();
			criteria.andWebidEqualTo(webid);
			thumbss.add((int)(webThumbsMapper.countByExample(example)));
		}
		return thumbss;
	}

}

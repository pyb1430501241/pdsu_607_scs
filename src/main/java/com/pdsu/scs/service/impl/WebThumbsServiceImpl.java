package com.pdsu.scs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.WebThumbsExample;
import com.pdsu.scs.bean.WebThumbsExample.Criteria;
import com.pdsu.scs.dao.WebThumbsMapper;
import com.pdsu.scs.service.WebThumbsService;

/**
 * 
 * @author 半梦
 *
 */
@Service("webThumbsService")
public class WebThumbsServiceImpl implements WebThumbsService {
	
	@Autowired
	private WebThumbsMapper webThumbsMapper;
	
	/**
	 * 根据网页ID集合获取这些文章的点赞数
	 */
	@Override
	public List<Integer> selectThumbssForWebId(List<Integer> webids) {
		List<Integer> thumbss = new ArrayList<Integer>();
		for(Integer webid : webids) {
			thumbss.add(selectThumbsForWebId(webid));
		}
		return thumbss;
	}

	/**
	 * 根据网页ID获取文章的点赞数
	 */
	@Override
	public Integer selectThumbsForWebId(Integer webid) {
		WebThumbsExample example = new WebThumbsExample();
		Criteria criteria = example.createCriteria();
		criteria.andWebidEqualTo(webid);
		long l = webThumbsMapper.countByExample(example);
		return (int)l;
	}

	/**
	 * 根据学号获取一个人的总点赞数
	 */
	@Override
	public Integer countThumbsByUid(Integer uid) {
		WebThumbsExample example = new WebThumbsExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return (int) webThumbsMapper.countByExample(example);
	}

}

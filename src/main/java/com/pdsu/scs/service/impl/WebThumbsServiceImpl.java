package com.pdsu.scs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.WebInformationExample;
import com.pdsu.scs.bean.WebThumbs;
import com.pdsu.scs.bean.WebThumbsExample;
import com.pdsu.scs.bean.WebThumbsExample.Criteria;
import com.pdsu.scs.dao.WebInformationMapper;
import com.pdsu.scs.dao.WebThumbsMapper;
import com.pdsu.scs.exception.web.blob.NotFoundBlobIdException;
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
	
	@Autowired
	private WebInformationMapper webinformationMapper;
	
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

	@Override
	public boolean insert(WebThumbs webThumbs) throws NotFoundBlobIdException {
		if(!countByWebId(webThumbs.getWebid())) {
			throw new NotFoundBlobIdException();
		}
		return webThumbsMapper.insertSelective(webThumbs) > 0 ? true : false;
	}

	@Override
	public boolean countByWebId(Integer webid) {
		WebInformationExample example = new WebInformationExample();
		com.pdsu.scs.bean.WebInformationExample.Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(webid);
		return webinformationMapper.countByExample(example) == 0 ? false : true;
	}

	@Override
	public boolean deletebyWebIdAndUid(Integer webid, Integer uid) {
		WebThumbsExample example = new WebThumbsExample();
		Criteria criteria = example.createCriteria();
		criteria.andWebidEqualTo(webid);
		criteria.andUidEqualTo(uid);
		return webThumbsMapper.deleteByExample(example) > 0 ? true : false;
	}
	
	@Override
	public boolean countByWebIdAndUid(Integer webid, Integer uid) {
		WebThumbsExample example = new WebThumbsExample();
		Criteria criteria = example.createCriteria();
		criteria.andWebidEqualTo(webid);
		criteria.andUidEqualTo(uid);
		return webThumbsMapper.countByExample(example) == 0 ? false : true;
	}
}

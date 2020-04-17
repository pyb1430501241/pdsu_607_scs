package com.pdsu.scs.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.WebInformation;
import com.pdsu.scs.bean.WebInformationExample;
import com.pdsu.scs.bean.WebInformationExample.Criteria;
import com.pdsu.scs.dao.WebInformationMapper;
import com.pdsu.scs.service.WebInformationService;

@Service("webInformationService")
public class WebInformationServiceImpl implements WebInformationService {

	@Autowired
	private WebInformationMapper webInformationMapper;
	
	/*
	 * 插入一个网页信息
	 */
	@Override
	public boolean insert(WebInformation information) {
		if(webInformationMapper.insertSelective(information) != 0) {
			return true;
		}
		return false;
	}

	/*
	 * 删除一个网页信息
	 */
	@Override
	public boolean deleteById(Integer id) {
		if(webInformationMapper.deleteByPrimaryKey(id) != 0) {
			return true;
		}
		return false;
	}

	/*
	 * 根据网页id查询一个网页的全部信息
	 */
	@Override
	public WebInformation selectById(Integer id) {
		return webInformationMapper.selectByPrimaryKey(id);
	}

	/*
	 * 根据时间排序查询文章
	 */
	@Override
	public List<WebInformation> selectWebInformationOrderByTimetest() {
		WebInformationExample example = new WebInformationExample();
		example.setOrderByClause("sub_time");
		List<WebInformation> selectByExampleWithBLOBs = webInformationMapper.selectByExampleWithBLOBs(example);
		for(WebInformation webInformation : selectByExampleWithBLOBs) {
			WebInformation web = webInformation;
			byte [] b = webInformation.getWebData();
			try {
				web.setWebDataString(new String(b,"utf-8"));
				web.setWebData(null);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return selectByExampleWithBLOBs;
	}

	/*
	 * 根据一个人的学号查询其所有文章, 不包括网页主体内容
	 */
	@Override
	public List<WebInformation> selectWebInformationsByUid(Integer uid) {
		WebInformationExample example = new WebInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return webInformationMapper.selectByExample(example);
	}

}

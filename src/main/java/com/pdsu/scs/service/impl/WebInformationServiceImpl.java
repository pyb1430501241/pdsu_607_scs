package com.pdsu.scs.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.UserInformationExample;
import com.pdsu.scs.bean.WebInformation;
import com.pdsu.scs.bean.WebInformationExample;
import com.pdsu.scs.bean.WebInformationExample.Criteria;
import com.pdsu.scs.dao.UserInformationMapper;
import com.pdsu.scs.dao.WebInformationMapper;
import com.pdsu.scs.exception.web.blob.NotFoundBlobIdException;
import com.pdsu.scs.exception.web.user.NotFoundUidException;
import com.pdsu.scs.service.WebInformationService;

/**
 * 处理博客页面
 * @author 半梦
 *
 */
@Service("webInformationService")
public class WebInformationServiceImpl implements WebInformationService {

	@Autowired
	private WebInformationMapper webInformationMapper;
	
	@Autowired
	private UserInformationMapper userInformationMapper;
	
	/*
	 * 插入一个网页信息
	 */
	@Override
	public boolean insert(WebInformation information) {
		if(webInformationMapper.insertSelective(information) > 0) {
			return true;
		}
		return false;
	}

	/*
	 * 删除一个网页信息
	 */
	@Override
	public boolean deleteById(Integer id) throws NotFoundBlobIdException{
		if(!countByWebId(id)) {
			throw new NotFoundBlobIdException("该文章不存在");
		}
		int i = webInformationMapper.deleteByPrimaryKey(id);
		if(i >= 0) {
			return true;
		}else {
			return false;
		}
	}

	/*
	 * 根据网页id查询一个网页的全部信息
	 */
	@Override
	public WebInformation selectById(Integer id) {
		WebInformation key = webInformationMapper.selectByPrimaryKey(id);
		return key;
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
	public List<WebInformation> selectWebInformationsByUid(Integer uid) throws NotFoundUidException {
		if(countByUid(uid) == 0) {
			throw new NotFoundUidException("该用户不存在");
		}
		WebInformationExample example = new WebInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		List<WebInformation> list = webInformationMapper.selectByExample(example);
		return list;
	}

	/*
	 *更新文章  
	 */
	@Override
	public boolean updateByWebId(WebInformation web) {
		WebInformationExample example = new WebInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(web.getId());
		int i = webInformationMapper.updateByExampleWithBLOBs(web, example);
		return i > 0 ? true : false;
	}

	/**
	 * 查询博客是否存在
	 */
	@Override
	public boolean countByWebId(Integer webid) {
		WebInformationExample example = new WebInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(webid);
		long l = webInformationMapper.countByExample(example);
		return l <= 0 ? false : true;
	}
	
	/**
	 * 查询是否有此账号
	 */
	@Override
	public int countByUid(Integer uid) {
		UserInformationExample example = new UserInformationExample();
		com.pdsu.scs.bean.UserInformationExample.Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return (int) userInformationMapper.countByExample(example);
	}

}

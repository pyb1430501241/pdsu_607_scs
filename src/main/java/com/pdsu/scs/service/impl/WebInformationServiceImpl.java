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
	
	@Override
	public boolean insert(WebInformation information) {
		if(webInformationMapper.insertSelective(information) != 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteById(Integer id) {
		if(webInformationMapper.deleteByPrimaryKey(id) != 0) {
			return true;
		}
		return false;
	}

	@Override
	public WebInformation selectById(Integer id) {
		return webInformationMapper.selectByPrimaryKey(id);
	}

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
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return selectByExampleWithBLOBs;
	}

}

package com.pdsu.scs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.WebLabel;
import com.pdsu.scs.bean.WebLabelExample;
import com.pdsu.scs.bean.WebLabelExample.Criteria;
import com.pdsu.scs.dao.WebLabelMapper;
import com.pdsu.scs.service.WebLabelService;

/**
 * @author 半梦
 */
@Service("webLabelService")
public class WebLabelServiceImpl implements WebLabelService {

	@Autowired
	private WebLabelMapper webLabelMapper;
	
	@Override
	public List<WebLabel> selectLabel() {
		return webLabelMapper.selectByExample(new WebLabelExample());
	}

	@Override
	public List<WebLabel> selectByLabelIds(List<Integer> labelids) {
		if(labelids == null || labelids.size() == 0) {
			return new ArrayList<WebLabel>();
		}
		WebLabelExample example = new WebLabelExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIn(labelids);
		return webLabelMapper.selectByExample(example);
	}

}

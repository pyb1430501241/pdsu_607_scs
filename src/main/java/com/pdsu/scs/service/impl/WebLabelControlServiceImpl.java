package com.pdsu.scs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.WebLabelControl;
import com.pdsu.scs.bean.WebLabelControlExample;
import com.pdsu.scs.bean.WebLabelControlExample.Criteria;
import com.pdsu.scs.dao.WebLabelControlMapper;
import com.pdsu.scs.service.WebLabelControlService;

/**
 * @author 半梦
 */
@Service("webLabelControlService")
public class WebLabelControlServiceImpl implements WebLabelControlService {

	@Autowired
	private WebLabelControlMapper webLabelControlMapper;
	
	@Override
	public boolean insert(Integer webid, List<Integer> labelList) {
		for (Integer labelid : labelList) {
			webLabelControlMapper.insertSelective(new WebLabelControl(webid, labelid));
		}
		return true;
	}

	@Override
	public boolean deleteByWebId(Integer webid) {
		WebLabelControlExample example = new WebLabelControlExample();
		Criteria criteria = example.createCriteria();
		criteria.andWidEqualTo(webid);
		return webLabelControlMapper.deleteByExample(example) > 0 ? true : false;
	}

	@Override
	public List<Integer> selectLabelIdByWebId(Integer webid) {
		WebLabelControlExample example = new WebLabelControlExample();
		Criteria criteria = example.createCriteria();
		criteria.andWidEqualTo(webid);
		List<WebLabelControl> list = webLabelControlMapper.selectByExample(example);
		List<Integer> labelIds = new ArrayList<>();
		for (WebLabelControl label : list) {
			labelIds.add(label.getLid());
		}
		return labelIds;
	}

}

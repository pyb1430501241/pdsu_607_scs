package com.pdsu.scs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.VisitInformation;
import com.pdsu.scs.bean.VisitInformationExample;
import com.pdsu.scs.bean.VisitInformationExample.Criteria;
import com.pdsu.scs.dao.VisitInformationMapper;
import com.pdsu.scs.service.VisitInformationService;

@Service("visitInformation")
public class VisitInformationImpl implements VisitInformationService {

	@Autowired
	private VisitInformationMapper visitInformationMapper;
	
	@Override
	public List<Integer> selectVisitsByWebIds(List<Integer> webids) {
		List<Integer> visits = new ArrayList<Integer>();
		for(Integer id : webids) {
			visits.add(selectvisitByWebId(id));
		}
		return visits;
	}
	
	public Integer selectVisitsByVid(Integer id) {
		VisitInformationExample example = new VisitInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andVidEqualTo(id);
		return (int)visitInformationMapper.countByExample(example);
	}

	@Override
	public boolean insert(VisitInformation visit) {
		if(visitInformationMapper.insert(visit) == 0) {
			return false;
		}
		return true;
	}

	@Override
	public Integer selectvisitByWebId(Integer webid) {
		VisitInformationExample example = new VisitInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andWidEqualTo(webid);
		return (int)visitInformationMapper.countByExample(example);
	}
}

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

/**
 * 
 * @author 半梦
 *
 */
@Service("visitInformation")
public class VisitInformationImpl implements VisitInformationService {

	@Autowired
	private VisitInformationMapper visitInformationMapper;
	
	
	/**
	 * 根据网页的ID集合获取这些文章的访问量
	 */
	@Override
	public List<Integer> selectVisitsByWebIds(List<Integer> webids) {
		List<Integer> visits = new ArrayList<Integer>();
		for(Integer id : webids) {
			visits.add(selectvisitByWebId(id));
		}
		return visits;
	}
	
	/**
	 * 获取一个人的总访问量
	 */
	public Integer selectVisitsByVid(Integer id) {
		VisitInformationExample example = new VisitInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andVidEqualTo(id);
		int i = (int) visitInformationMapper.countByExample(example);
		return i;
	}

	/**
	 * 插入一次访问记录
	 */
	@Override
	public boolean insert(VisitInformation visit) {
		if(visitInformationMapper.insert(visit) > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 根据网页ID获取网页访问量
	 */
	@Override
	public Integer selectvisitByWebId(Integer webid) {
		VisitInformationExample example = new VisitInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andWidEqualTo(webid);
		return (int)visitInformationMapper.countByExample(example);
	}
}

package com.pdsu.scs.service;

import java.util.List;

import com.pdsu.scs.bean.VisitInformation;

public interface VisitInformationService {
	
	//根据网页id集获取该网页的访问量集
	public List<Integer> selectVisitsByWebIds(List<Integer> webids);
	
	//根据一个人的uid来获取其总访问量
	public Integer selectVisitsByVid(Integer id);
	
	//插入一条记录
	public boolean insert(VisitInformation visit);
	
	//根据网页id获取该网页访问量
	public Integer selectvisitByWebId(Integer webid);
}
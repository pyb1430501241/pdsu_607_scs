package com.pdsu.scs.service;

import java.util.List;

import com.pdsu.scs.bean.MyCollection;

public interface MyCollectionService {
	
	//插入
	public boolean insert(MyCollection con);
	
	//根据网页id获取此网页的收藏量
	public Integer selectCollectionsByWebId(Integer webid);

	//根据一组网页id获取集群
	public List<Integer> selectCollectionssByWebIds(List<Integer> webids);
	
}

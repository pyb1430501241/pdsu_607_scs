package com.pdsu.scs.service;

import java.util.List;

import com.pdsu.scs.bean.MyCollection;

/**
 * 该接口提供和收藏相关的方法
 * @author 半梦
 *
 */
public interface MyCollectionService {
	
	//插入一个收藏记录
	public boolean insert(MyCollection con);
	
	//根据网页id获取此网页的收藏量
	public Integer selectCollectionsByWebId(Integer webid);

	//根据一组网页id获取集群
	public List<Integer> selectCollectionssByWebIds(List<Integer> webids);

	//删除一个收藏记录
	public boolean delete(Integer uid, Integer webid);
	
}

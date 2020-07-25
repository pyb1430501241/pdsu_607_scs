package com.pdsu.scs.service;

import java.util.List;

/**
 * 该接口负责提供点赞相关的方法
 * @author 半梦
 *
 */
public interface WebThumbsService {
	
	/**
	 * 根据网页ID集合获取这些文章的点赞数
	 */
	public List<Integer> selectThumbssForWebId(List<Integer> webids);

	/**
	 * 根据网页ID获取文章的点赞数
	 */
	public Integer selectThumbsForWebId(Integer webid);

	/**
	 * 根据学号获取一个人的总点赞数
	 */
	public Integer countThumbsByUid(Integer uid);
	
}

package com.pdsu.scs.service;

import java.util.List;

/**
 * 该接口负责提供点赞相关的方法
 * @author 半梦
 *
 */
public interface WebThumbsService {
	
	//根据文章id集获取每篇文章的点赞数
	public List<Integer> selectThumbssForWebId(List<Integer> webids);

	//根据文章的id获取文章点赞数
	public Integer selectThumbsForWebId(Integer webid);
	
}

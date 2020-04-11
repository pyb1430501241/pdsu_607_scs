package com.pdsu.scs.service;

import java.util.List;

/**
 * 该接口负责提供点赞相关的方法
 * @author Admin
 *
 */
public interface WebThumbsService {
	
	//根据文章id集获取每篇文章的点赞数
	public List<Integer> selectThumbssForWebId(List<Integer> webids);
	
}

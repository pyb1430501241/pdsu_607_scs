package com.pdsu.scs.service;

import java.util.List;

import com.pdsu.scs.bean.Contype;

/**
 * @author 半梦
 */
public interface ContypeService {

	/**
	 * 获取文章类型列表
	 * @return
	 */
	public List<Contype> selectContypes();
	
}

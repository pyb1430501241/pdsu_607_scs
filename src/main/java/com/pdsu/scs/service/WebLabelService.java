package com.pdsu.scs.service;

import java.util.List;

import com.pdsu.scs.bean.WebLabel;

/**
 * @author 半梦
 */
public interface WebLabelService {
	
	/**
	 * 获取所有标签
	 * @return
	 */
	public List<WebLabel> selectLabel();

	/**
	 * 获取对应的标签信息
	 * @param labelids
	 * @return
	 */
	public List<WebLabel> selectByLabelIds(List<Integer> labelids);
	
}

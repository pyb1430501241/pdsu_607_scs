package com.pdsu.scs.service;

import java.util.List;

/**
 * @author 半梦
 */
public interface WebLabelControlService {

	/**
	 * 插入文章和标签的对应
	 * @param id
	 * @param labelList
	 * @return
	 */
	public boolean insert(Integer webid, List<Integer> labelList);
	
	/**
	 * 删除文章标签
	 * @param id
	 * @return
	 */
	public boolean deleteByWebId(Integer webid);

	/**
	 * 获取文章标签ID
	 * @param id
	 * @return
	 */
	public List<Integer> selectLabelIdByWebId(Integer webid);

	/**
	 * 根据标签获取文章
	 * @param lid
	 * @return
	 */
	public List<Integer> selectWebIdsByLid(Integer lid);

}

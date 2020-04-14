package com.pdsu.scs.service;

import java.util.List;

import com.pdsu.scs.bean.WebInformation;

/**
 * 与网页相关的方法
 * @author Admin
 *
 */
public interface WebInformationService {

	/**
	 * 插入一个网页
	 * @param information
	 * @return  是否插入成功
	 */
	public boolean insert(WebInformation information);
	
	/**
	 * 根据网页id删除一个网页
	 * @param id
	 * @return
	 */
	public boolean deleteById(Integer id);
	
	/**
	 * 根据网页id查询网页信息
	 * @param id   网页编号
	 * @return
	 */
	public WebInformation selectById(Integer id);
	
	/**
	 * 根据时间查询网页集合
	 * 待定, 回头会加入热度算法
	 * @return
	 */
	public List<WebInformation> selectWebInformationOrderByTimetest();

	/**
	 * 根据作者学号查询网页集
	 * @param uid  学号
	 * @return
	 */
	public List<WebInformation> selectWebInformationsByUid(Integer uid);
	
}

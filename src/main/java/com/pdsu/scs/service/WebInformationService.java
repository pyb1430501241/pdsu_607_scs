package com.pdsu.scs.service;

import java.util.List;

import com.pdsu.scs.bean.WebInformation;

/**
 * 与网页相关的方法
 * @author Admin
 *
 */
public interface WebInformationService {

	public boolean insert(WebInformation information);
	
	public boolean deleteById(Integer id);
	
	public WebInformation selectById(Integer id);
	
	public List<WebInformation> selectWebInformationOrderByTimetest();

	public List<WebInformation> selectWebInformationsByUid(Integer uid);
	
}

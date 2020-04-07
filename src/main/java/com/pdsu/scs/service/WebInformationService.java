package com.pdsu.scs.service;

import com.pdsu.scs.bean.WebInformation;

public interface WebInformationService {

	public boolean insert(WebInformation information);
	
	public boolean deleteById(Integer id);
	
	public WebInformation selectById(Integer id);
	
}

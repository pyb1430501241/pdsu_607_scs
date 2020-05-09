package com.pdsu.scs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.WebFile;
import com.pdsu.scs.dao.WebFileMapper;
import com.pdsu.scs.service.WebFileService;

@Service("webFileService")
public class WebFileServiceImpl implements WebFileService{

	@Autowired
	private WebFileMapper webFileMapper;
	
	@Override
	public boolean insert(WebFile webFile) {
		int t = webFileMapper.insertSelective(webFile);
		return t > 0 ? true : false;
	}

}

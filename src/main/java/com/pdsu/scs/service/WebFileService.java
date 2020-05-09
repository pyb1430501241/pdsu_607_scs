package com.pdsu.scs.service;

import com.pdsu.scs.bean.WebFile;

/**
 * 
 * @author 半梦
 *
 */
public interface WebFileService {

	/**
	 * 插入一个文件记录
	 * @param webFile
	 * @return  true false
	 */
	public boolean insert(WebFile webFile);
	
}

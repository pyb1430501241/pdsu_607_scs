package com.pdsu.scs.service;

import com.pdsu.scs.bean.WebFile;
import com.pdsu.scs.exception.web.file.FileException;
import com.pdsu.scs.exception.web.file.UidAndTItleRepetitionException;

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
	public boolean insert(WebFile webFile) throws UidAndTItleRepetitionException;

	/**
	 * 查询所有文件
	 * @param uid
	 * @param title
	 * @return
	 * @throws FileException
	 */
	public WebFile selectFileByUidAndTitle(Integer uid, String title);
	
	/**
	 * 查询符合条件的文件是否存在
	 * @param uid
	 * @param title
	 * @return
	 */
	public boolean countByUidAndTitle(Integer uid, String title);
	
}
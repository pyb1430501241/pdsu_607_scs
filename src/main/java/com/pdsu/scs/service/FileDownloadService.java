package com.pdsu.scs.service;

import java.util.List;

import com.pdsu.scs.bean.FileDownload;
import org.springframework.lang.NonNull;

/**
 * @author 半梦
 */
public interface FileDownloadService {
	
	/**
	 * 插入下载记录
	 * @param fileDownload
	 * @return
	 */
	public boolean insert(@NonNull FileDownload fileDownload);
	
	/**
	 * 获取用户文件被下载量
	 * @param uid
	 * @return
	 */
	public Integer countByBid(@NonNull Integer uid);

	/**
	 * 获取文件下载量
	 * @param fileids
	 * @return
	 */
	public List<Integer> selectDownloadsByFileIds(@NonNull List<Integer> fileids);
	
}
	
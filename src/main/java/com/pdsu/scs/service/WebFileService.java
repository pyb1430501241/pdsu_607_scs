package com.pdsu.scs.service;

import java.util.List;

import com.pdsu.scs.bean.WebFile;
import com.pdsu.scs.exception.web.es.InsertException;
import com.pdsu.scs.exception.web.file.FileException;
import com.pdsu.scs.exception.web.file.UidAndTItleRepetitionException;
import org.springframework.lang.NonNull;

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
	public boolean insert(@NonNull WebFile webFile) throws UidAndTItleRepetitionException, InsertException;

	/**
	 * 查询所有文件
	 * @param uid
	 * @param title
	 * @return
	 * @throws FileException
	 */
	public WebFile selectFileByUidAndTitle(@NonNull Integer uid, @NonNull String title);
	
	/**
	 * 查询符合条件的文件是否存在
	 * @param uid
	 * @param title
	 * @return
	 */
	public boolean countByUidAndTitle(@NonNull Integer uid, @NonNull String title);

	/**
	 * 获取用户上传文件总量
	 * @param uid
	 * @return
	 */
	public Integer countByUid(@NonNull Integer uid);

	/**
	 * 获取文件首页数据
	 * @return
	 */
	public List<WebFile> selectFilesOrderByTime();

	/**
	 * 根据fileid获取文件呢信息
	 * @param fileids
	 * @return
	 */
    public List<WebFile> selectFilesByFileIds(@NonNull List<Integer> fileids);
}

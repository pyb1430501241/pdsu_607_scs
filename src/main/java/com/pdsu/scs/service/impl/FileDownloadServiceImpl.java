package com.pdsu.scs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.FileDownload;
import com.pdsu.scs.bean.FileDownloadExample;
import com.pdsu.scs.bean.FileDownloadExample.Criteria;
import com.pdsu.scs.dao.FileDownloadMapper;
import com.pdsu.scs.service.FileDownloadService;

/**
 * @author 半梦
 */
@Service("fileDownloadService")
public class FileDownloadServiceImpl implements FileDownloadService {

	@Autowired
	private FileDownloadMapper fileDownloadMapper;
	
	@Override
	public boolean insert(FileDownload fileDownload) {
		return fileDownloadMapper.insertSelective(fileDownload) > 0 ? true : false;
	}

	@Override
	public Integer countByBid(Integer uid) {
		FileDownloadExample example = new FileDownloadExample();
		Criteria criteria = example.createCriteria();
		criteria.andBidEqualTo(uid);
		return (int) fileDownloadMapper.countByExample(example);
	}

}

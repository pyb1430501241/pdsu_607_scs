package com.pdsu.scs.service.impl;

import java.util.ArrayList;
import java.util.List;

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

	@Override
	public List<Integer> selectDownloadsByFileIds(List<Integer> fileids) {
		if(fileids == null || fileids.size() == 0) {
			return new ArrayList<Integer>();
		}
		FileDownloadExample example = new FileDownloadExample();
		Criteria criteria = example.createCriteria();
		List<Integer> list = new ArrayList<Integer>();
		for (Integer fileid : fileids) {
			criteria.andFileidEqualTo(fileid);
			list.add((int)fileDownloadMapper.countByExample(example));
		}
		return list;
	}

}

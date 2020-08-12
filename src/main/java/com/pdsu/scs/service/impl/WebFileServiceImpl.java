package com.pdsu.scs.service.impl;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.EsFileInformation;
import com.pdsu.scs.bean.WebFile;
import com.pdsu.scs.bean.WebFileExample;
import com.pdsu.scs.bean.WebFileExample.Criteria;
import com.pdsu.scs.dao.WebFileMapper;
import com.pdsu.scs.es.dao.EsDao;
import com.pdsu.scs.exception.web.es.InsertException;
import com.pdsu.scs.exception.web.file.FileException;
import com.pdsu.scs.exception.web.file.UidAndTItleRepetitionException;
import com.pdsu.scs.service.WebFileService;

@Service("webFileService")
public class WebFileServiceImpl implements WebFileService{

	@Autowired
	private WebFileMapper webFileMapper;
	
	@Autowired
	private EsDao esDao;
	
	/**
	 * 插入一个文件记录
	 * @param webFile
	 * @return  true false
	 * @throws UidAndTItleRepetitionException 
	 * @throws InsertException 
	 */
	@Override
	public boolean insert(WebFile webFile) throws UidAndTItleRepetitionException, InsertException {
		if(countByUidAndTitle(webFile.getUid(), webFile.getTitle())) {
			throw new UidAndTItleRepetitionException("用户无法重复上传同名文件");
		}
		int t = webFileMapper.insertSelective(webFile);
		if(t > 0) {
			EsFileInformation file = new EsFileInformation(webFile.getDescription(), 
					webFile.getTitle(), webFile.getId());
			return esDao.insert(file, webFile.getId());
		}
		return false;
	}

	/**
	 * 查询指定文件
	 * @param uid
	 * @param title
	 * @return
	 * @throws FileException
	 */
	@Override
	public WebFile selectFileByUidAndTitle(Integer uid, String title) {
		WebFileExample example = new WebFileExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		criteria.andTitleEqualTo(title);
		List<WebFile> list = webFileMapper.selectByExample(example);
		if(list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * 查询符合条件的文件是否存在
	 * @param uid
	 * @param title
	 * @return
	 */
	@Override
	public boolean countByUidAndTitle(Integer uid, String title) {
		WebFileExample example = new WebFileExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		criteria.andTitleEqualTo(title);
		return webFileMapper.countByExample(example) <= 0 ? false : true;
	}

	@Override
	public Integer countByUid(Integer uid) {
		WebFileExample example = new WebFileExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return (int) webFileMapper.countByExample(example);
	}

	@Override
	public List<WebFile> selectFilesOrderByTime() {
		WebFileExample example = new WebFileExample();
		example.setOrderByClause("creattime DESC");
		return webFileMapper.selectByExample(example);
	}

	@Override
	public List<WebFile> selectFilesByFileIds(List<Integer> fileids) {
		if(fileids == null || fileids.size() == 0) {
			return new ArrayList<>();
		}
		WebFileExample example = new WebFileExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIn(fileids);
		return webFileMapper.selectByExample(example);
	}

}

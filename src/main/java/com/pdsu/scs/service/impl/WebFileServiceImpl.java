package com.pdsu.scs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.WebFile;
import com.pdsu.scs.bean.WebFileExample;
import com.pdsu.scs.bean.WebFileExample.Criteria;
import com.pdsu.scs.dao.WebFileMapper;
import com.pdsu.scs.exception.web.file.FileException;
import com.pdsu.scs.exception.web.file.UidAndTItleRepetitionException;
import com.pdsu.scs.service.WebFileService;

@Service("webFileService")
public class WebFileServiceImpl implements WebFileService{

	@Autowired
	private WebFileMapper webFileMapper;
	
	/**
	 * 插入一个文件记录
	 * @param webFile
	 * @return  true false
	 * @throws UidAndTItleRepetitionException 
	 */
	@Override
	public boolean insert(WebFile webFile) throws UidAndTItleRepetitionException {
		if(countByUidAndTitle(webFile.getUid(), webFile.getTitle())) {
			throw new UidAndTItleRepetitionException("用户无法重复上传同名文件");
		}
		int t = webFileMapper.insertSelective(webFile);
		return t > 0 ? true : false;
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

}

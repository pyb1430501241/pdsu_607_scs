package com.pdsu.scs.service.impl;

import com.pdsu.scs.bean.WebComment;
import com.pdsu.scs.bean.WebCommentExample;
import com.pdsu.scs.bean.WebCommentExample.Criteria;
import com.pdsu.scs.bean.WebInformation;
import com.pdsu.scs.bean.WebInformationExample;
import com.pdsu.scs.dao.WebCommentMapper;
import com.pdsu.scs.dao.WebInformationMapper;
import com.pdsu.scs.exception.web.blob.NotFoundBlobIdException;
import com.pdsu.scs.service.WebCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author 半梦
 *
 */

@Service("webCommentService")
public class WebCommentServiceImpl implements WebCommentService{

	@Autowired
	private WebCommentMapper webCommentMapper;
	
	@Autowired
	private WebInformationMapper webInformationMapper;
	
	@Override
	public boolean insert(@NonNull WebComment webComment) throws NotFoundBlobIdException {
		if(!countByWebid(webComment.getWid())) {
			throw new NotFoundBlobIdException();
		}
		return webCommentMapper.insertSelective(webComment) != 0;
	}

	@Override
	public boolean countByWebid(@NonNull Integer webid) {
		WebInformationExample example = new WebInformationExample();
		com.pdsu.scs.bean.WebInformationExample.Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(webid);
		return webInformationMapper.countByExample(example) != 0;
	}

	@Override
	public List<WebComment> selectCommentsByWebId(@NonNull Integer webid) throws NotFoundBlobIdException {
		if(!countByWebid(webid)) {
			throw new NotFoundBlobIdException();
		}
		WebCommentExample example = new WebCommentExample();
		Criteria criteria = example.createCriteria();
		criteria.andWidEqualTo(webid);
		return webCommentMapper.selectByExample(example);
	}

	@Override
	public Integer countByUid(@NonNull Integer uid) {
		WebInformationExample example = new WebInformationExample();
		com.pdsu.scs.bean.WebInformationExample.Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		List<WebInformation> list = webInformationMapper.selectByExample(example);
		List<Integer> webids = new ArrayList<Integer>();
		for (WebInformation webInformation : list) {
			webids.add(webInformation.getId());
		}
		if(webids.size() == 0) {
			return 0;
		}
		WebCommentExample example2 = new WebCommentExample();
		Criteria createCriteria = example2.createCriteria();
		createCriteria.andWidIn(webids);
		return (int) webCommentMapper.countByExample(example2);
	}

	@Override
	public WebComment selectCommentById(Integer cid) {
		return webCommentMapper.selectByPrimaryKey(cid);
	}

}

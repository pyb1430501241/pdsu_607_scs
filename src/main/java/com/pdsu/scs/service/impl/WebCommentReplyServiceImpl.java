package com.pdsu.scs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.WebComment;
import com.pdsu.scs.bean.WebCommentExample;
import com.pdsu.scs.bean.WebCommentReply;
import com.pdsu.scs.bean.WebCommentReplyExample;
import com.pdsu.scs.bean.WebCommentReplyExample.Criteria;
import com.pdsu.scs.bean.WebInformationExample;
import com.pdsu.scs.dao.WebCommentMapper;
import com.pdsu.scs.dao.WebCommentReplyMapper;
import com.pdsu.scs.dao.WebInformationMapper;
import com.pdsu.scs.exception.web.blob.NotFoundBlobIdException;
import com.pdsu.scs.exception.web.blob.comment.NotFoundCommentIdException;
import com.pdsu.scs.service.WebCommentReplyService;

/**
 * 
 * @author 半梦
 *
 */
@Service("webCommentReplyService")
public class WebCommentReplyServiceImpl implements WebCommentReplyService{

	@Autowired
	private WebCommentReplyMapper webCommentReplyMapper;
	
	@Autowired
	private WebInformationMapper webInformationMapper;
	
	@Autowired
	private WebCommentMapper webCommentMapper;
	
	@Override
	public boolean insert(WebCommentReply webCommentReply) throws NotFoundBlobIdException, NotFoundCommentIdException {
		if(!countByWebid(webCommentReply.getWid())) {
			throw new NotFoundBlobIdException();
		}
		if(!countByCid(webCommentReply.getCid())) {
			throw new NotFoundCommentIdException();
		}
		return webCommentReplyMapper.insertSelective(webCommentReply) == 0 ? false : true;
	}

	@Override
	public boolean countByWebid(Integer webid) {
		WebInformationExample example = new WebInformationExample();
		com.pdsu.scs.bean.WebInformationExample.Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(webid);
		return webInformationMapper.countByExample(example) == 0 ? false : true;
	}

	@Override
	public boolean countByCid(Integer cid) {
		WebCommentExample example = new WebCommentExample();
		com.pdsu.scs.bean.WebCommentExample.Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(cid);
		return webCommentMapper.countByExample(example) == 0 ? false : true;
	}

	@Override
	public List<WebCommentReply> selectCommentReplysByWebComments(List<WebComment> commentList) {
		if(commentList.size() == 0) {
			return new ArrayList<>();
		}
		List<Integer> commentid = new ArrayList<>();
		for(WebComment w : commentList) {
			commentid.add(w.getId());
		}
		WebCommentReplyExample example = new WebCommentReplyExample();
		Criteria criteria = example.createCriteria();
		criteria.andCidIn(commentid);
		return webCommentReplyMapper.selectByExample(example);
	}
	
	@Override
	public List<WebCommentReply> selectCommentReplysByWebId(Integer webid) throws NotFoundBlobIdException {
		if(!countByWebid(webid)) {
			throw new NotFoundBlobIdException();
		}
		WebCommentReplyExample example = new WebCommentReplyExample();
		Criteria criteria = example.createCriteria();
		criteria.andWidEqualTo(webid);
		return webCommentReplyMapper.selectByExample(example);
	}

	@Override
	public Integer countByWebsAndUid(List<Integer> webs) {
		if(webs == null || webs.size() == 0) {
			return 0;
		}
		WebCommentReplyExample example = new WebCommentReplyExample();
		Criteria criteria = example.createCriteria();
		criteria.andWidIn(webs);
		return (int) webCommentReplyMapper.countByExample(example);
	}
	
}

package com.pdsu.scs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.MyCollection;
import com.pdsu.scs.bean.MyCollectionExample;
import com.pdsu.scs.bean.WebInformationExample;
import com.pdsu.scs.bean.MyCollectionExample.Criteria;
import com.pdsu.scs.dao.MyCollectionMapper;
import com.pdsu.scs.dao.WebInformationMapper;
import com.pdsu.scs.exception.web.blob.NotFoundBlobIdException;
import com.pdsu.scs.exception.web.user.UidAndWebIdRepetitionException;
import com.pdsu.scs.service.MyCollectionService;

/**
 *
 * @author 半梦
 *
 */
@Service("myCollectionService")
public class MyCollectionServiceImpl implements MyCollectionService{

	@Autowired
	private MyCollectionMapper myCollectionMapper;
	
	@Autowired
	private WebInformationMapper webInformationMapper;

	@Override
	public boolean insert(MyCollection con) throws UidAndWebIdRepetitionException {
		if(countByUidAndWebId(con.getUid(), con.getWid())) {
			throw new UidAndWebIdRepetitionException("不可重复收藏同一篇博客");
		}
		if(myCollectionMapper.insertSelective(con) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Integer selectCollectionsByWebId(Integer webid) throws NotFoundBlobIdException {
		if(!countByWebId(webid)) {
			throw new NotFoundBlobIdException("该文章不存在");
		}
		MyCollectionExample example = new MyCollectionExample();
		Criteria criteria = example.createCriteria();
		criteria.andWidEqualTo(webid);
		return (int) myCollectionMapper.countByExample(example);
	}

	@Override
	public List<Integer> selectCollectionssByWebIds(List<Integer> webids){
		List<Integer> collectionss = new ArrayList<Integer>();
		for(Integer webid : webids) {
			try {
				collectionss.add(selectCollectionsByWebId(webid));
			} catch (NotFoundBlobIdException e) {
			}
		}
		return collectionss;
	}

	@Override
	public boolean delete(Integer uid, Integer webid) throws UidAndWebIdRepetitionException {
		if(!countByUidAndWebId(uid, webid)) {
			throw new UidAndWebIdRepetitionException("你并没有收藏该文章");
		}
		MyCollectionExample example = new MyCollectionExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		criteria.andWidEqualTo(webid);
		if(myCollectionMapper.deleteByExample(example) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean countByUidAndWebId(Integer uid, Integer webid) {
		MyCollectionExample example = new MyCollectionExample();
		Criteria criteria = example.createCriteria();
		criteria.andWidEqualTo(webid);
		criteria.andUidEqualTo(uid);
		return myCollectionMapper.countByExample(example) > 0 ? true : false;
	}
	

	/**
	 * 查询博客是否存在
	 */
	@Override
	public boolean countByWebId(Integer webid) {
		WebInformationExample example = new WebInformationExample();
		com.pdsu.scs.bean.WebInformationExample.Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(webid);
		long l = webInformationMapper.countByExample(example);
		return l <= 0 ? false : true;
	}

	@Override
	public Integer countCollectionByUid(Integer uid) {
		MyCollectionExample example = new MyCollectionExample();
		Criteria criteria = example.createCriteria();
		criteria.andBidEqualTo(uid);
		return (int) myCollectionMapper.countByExample(example);
	}
}

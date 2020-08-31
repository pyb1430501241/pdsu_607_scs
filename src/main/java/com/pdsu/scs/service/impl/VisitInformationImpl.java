package com.pdsu.scs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.UserInformationExample;
import com.pdsu.scs.bean.VisitInformation;
import com.pdsu.scs.bean.VisitInformationExample;
import com.pdsu.scs.bean.VisitInformationExample.Criteria;
import com.pdsu.scs.bean.WebInformationExample;
import com.pdsu.scs.dao.UserInformationMapper;
import com.pdsu.scs.dao.VisitInformationMapper;
import com.pdsu.scs.dao.WebInformationMapper;
import com.pdsu.scs.es.dao.EsDao;
import com.pdsu.scs.exception.web.blob.NotFoundBlobIdException;
import com.pdsu.scs.exception.web.es.UpdateException;
import com.pdsu.scs.exception.web.user.NotFoundUidException;
import com.pdsu.scs.service.VisitInformationService;

/**
 * 
 * @author 半梦
 *
 */
@Service("visitInformation")
public class VisitInformationImpl implements VisitInformationService {

	@Autowired
	private VisitInformationMapper visitInformationMapper;
	
	@Autowired
	private WebInformationMapper webInformationMapper;
	
	@Autowired
	private UserInformationMapper userInformationMapper;
	
	/**
	 * 根据网页的ID集合获取这些文章的访问量
	 * 如果文章不存在, 则剔除集合
	 */
	@Override
	public List<Integer> selectVisitsByWebIds(@NonNull List<Integer> webids) {
		List<Integer> visits = new ArrayList<Integer>();
		for(Integer id : webids) {
			try {
				visits.add(selectvisitByWebId(id));
			} catch (NotFoundBlobIdException e) {
			}
		}
		return visits;
	}
	
	/**
	 * 获取一个人的总访问量
	 * @throws NotFoundUidException 
	 */
	public Integer selectVisitsByVid(@NonNull Integer id) throws NotFoundUidException {
		if(!countByUid(id)) {
			throw new NotFoundUidException("该用户不存在");
		}
		VisitInformationExample example = new VisitInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andVidEqualTo(id);
		int i = (int) visitInformationMapper.countByExample(example);
		return i;
	}

	/**
	 * 插入一次访问记录
	 * @throws UpdateException 
	 */
	@Override
	public boolean insert(@NonNull VisitInformation visit) {
		if(visitInformationMapper.insert(visit) > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 根据网页ID获取网页访问量
	 * @throws NotFoundBlobIdException 
	 */
	@Override
	public Integer selectvisitByWebId(@NonNull Integer webid) throws NotFoundBlobIdException {
		if(!countByWebId(webid)) {
			throw new NotFoundBlobIdException("该文章不存在");
		}
		VisitInformationExample example = new VisitInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andWidEqualTo(webid);
		return (int)visitInformationMapper.countByExample(example);
	}

	@Override
	public boolean countByWebId(@NonNull Integer webid) {
		WebInformationExample example = new WebInformationExample();
		com.pdsu.scs.bean.WebInformationExample.Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(webid);
		return webInformationMapper.countByExample(example) > 0 ? true : false;
	}

	@Override
	public boolean countByUid(@NonNull Integer uid) {
		UserInformationExample example = new UserInformationExample();
		com.pdsu.scs.bean.UserInformationExample.Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return userInformationMapper.countByExample(example) > 0 ? true : false;
	}
}

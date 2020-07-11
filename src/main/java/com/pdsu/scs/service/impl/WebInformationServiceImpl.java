package com.pdsu.scs.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.EsBlobInformation;
import com.pdsu.scs.bean.MyCollectionExample;
import com.pdsu.scs.bean.UserInformationExample;
import com.pdsu.scs.bean.VisitInformationExample;
import com.pdsu.scs.bean.WebInformation;
import com.pdsu.scs.bean.WebInformationExample;
import com.pdsu.scs.bean.WebInformationExample.Criteria;
import com.pdsu.scs.bean.WebThumbsExample;
import com.pdsu.scs.dao.MyCollectionMapper;
import com.pdsu.scs.dao.UserInformationMapper;
import com.pdsu.scs.dao.VisitInformationMapper;
import com.pdsu.scs.dao.WebInformationMapper;
import com.pdsu.scs.dao.WebThumbsMapper;
import com.pdsu.scs.es.dao.EsDao;
import com.pdsu.scs.exception.web.DeleteInforException;
import com.pdsu.scs.exception.web.blob.NotFoundBlobIdException;
import com.pdsu.scs.exception.web.es.InsertException;
import com.pdsu.scs.exception.web.user.NotFoundUidException;
import com.pdsu.scs.service.WebInformationService;

/**
 * 处理博客页面
 * @author 半梦
 *
 */
@Service("webInformationService")
public class WebInformationServiceImpl implements WebInformationService {

	@Autowired
	private WebInformationMapper webInformationMapper;
	
	@Autowired
	private UserInformationMapper userInformationMapper;
	
	@Autowired
	private WebThumbsMapper webThumbsMapper;
	
	@Autowired
	private MyCollectionMapper myCollectionMapper;
	
	@Autowired
	private VisitInformationMapper visitInformationMapper;
	
	@Autowired
	private EsDao esDao;
	
	/*
	 * 插入一个网页信息
	 */
	@Override
	public boolean insert(WebInformation information) throws InsertException {
		if(webInformationMapper.insertSelective(information) > 0) {
			EsBlobInformation blob = new EsBlobInformation(information.getId(), 
					information.getWebDataString().substring(0, 30), information.getTitle());
			return esDao.insert(blob, information.getId());
		}
		return false;
	}

	/*
	 * 删除一个网页信息
	 */
	@Override
	public boolean deleteById(Integer id) throws NotFoundBlobIdException, DeleteInforException{
		if(!countByWebId(id)) {
			throw new NotFoundBlobIdException("该文章不存在");
		}
		
		/**
		 * 删除和网页相关的收藏信息 
		 */
		MyCollectionExample myCollectionExample = new MyCollectionExample();
		com.pdsu.scs.bean.MyCollectionExample.Criteria myCollectionCriteria1 = myCollectionExample.createCriteria();
		myCollectionCriteria1.andWidEqualTo(id);
		//获取和用户相关的收藏信息总数
		long mycollectionCount = myCollectionMapper.countByExample(myCollectionExample);
		if(myCollectionMapper.deleteByExample(myCollectionExample) != mycollectionCount) {
			throw new DeleteInforException("删除网页收藏信息失败");
		}
		
		/**
		 *删除和网页相关的访问信息 
		 */
		VisitInformationExample visitInformationExample = new VisitInformationExample();
		com.pdsu.scs.bean.VisitInformationExample.Criteria visitInformationCriteria1 = visitInformationExample.createCriteria();
		visitInformationCriteria1.andWidEqualTo(id);
		long visitCount = visitInformationMapper.countByExample(visitInformationExample);
		if(visitInformationMapper.deleteByExample(visitInformationExample) != visitCount) {
			throw new DeleteInforException("删除网页访问信息失败");
		}
		
		/**
		 *删除用户相关的点赞信息
		 */
		WebThumbsExample webThumbsExample = new WebThumbsExample();
		com.pdsu.scs.bean.WebThumbsExample.Criteria webThumbsCriteria1 = webThumbsExample.createCriteria();
		webThumbsCriteria1.andWebidEqualTo(id);
		long webThumbsCount = webThumbsMapper.countByExample(webThumbsExample);
		if(webThumbsMapper.deleteByExample(webThumbsExample) != webThumbsCount) {
			throw new DeleteInforException("删除用户点赞信息失败");
		}
		
		int i = webInformationMapper.deleteByPrimaryKey(id);
		if(i >= 0) {
			return true;
		}else {
			throw new DeleteInforException("删除网页失败");
		}
	}

	/*
	 * 根据网页id查询一个网页的全部信息
	 */
	@Override
	public WebInformation selectById(Integer id) {
		WebInformation key = webInformationMapper.selectByPrimaryKey(id);
		return key;
	}

	/*
	 * 根据时间排序查询文章
	 */
	@Override
	public List<WebInformation> selectWebInformationOrderByTimetest() {
		WebInformationExample example = new WebInformationExample();
		example.setOrderByClause("sub_time");
		List<WebInformation> selectByExampleWithBLOBs = webInformationMapper.selectByExampleWithBLOBs(example);
		for(WebInformation webInformation : selectByExampleWithBLOBs) {
			WebInformation web = webInformation;
			byte [] b = webInformation.getWebData();
			try {
				web.setWebDataString(new String(b,"utf-8"));
				web.setWebData(null);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return selectByExampleWithBLOBs;
	}

	/*
	 * 根据一个人的学号查询其所有文章, 不包括网页主体内容
	 */
	@Override
	public List<WebInformation> selectWebInformationsByUid(Integer uid) throws NotFoundUidException {
		if(countByUid(uid) == 0) {
			throw new NotFoundUidException("该用户不存在");
		}
		WebInformationExample example = new WebInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		List<WebInformation> list = webInformationMapper.selectByExample(example);
		return list;
	}

	/*
	 *更新文章  
	 */
	@Override
	public boolean updateByWebId(WebInformation web) {
		WebInformationExample example = new WebInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(web.getId());
		int i = webInformationMapper.updateByExampleWithBLOBs(web, example);
		return i > 0 ? true : false;
	}

	/**
	 * 查询博客是否存在
	 */
	@Override
	public boolean countByWebId(Integer webid) {
		WebInformationExample example = new WebInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(webid);
		long l = webInformationMapper.countByExample(example);
		return l <= 0 ? false : true;
	}
	
	/**
	 * 查询是否有此账号
	 */
	@Override
	public int countByUid(Integer uid) {
		UserInformationExample example = new UserInformationExample();
		com.pdsu.scs.bean.UserInformationExample.Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return (int) userInformationMapper.countByExample(example);
	}

}

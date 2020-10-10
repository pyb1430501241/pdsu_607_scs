package com.pdsu.scs.service.impl;

import com.pdsu.scs.bean.*;
import com.pdsu.scs.bean.WebInformationExample.Criteria;
import com.pdsu.scs.dao.*;
import com.pdsu.scs.es.dao.EsDao;
import com.pdsu.scs.exception.web.DeleteInforException;
import com.pdsu.scs.exception.web.blob.NotFoundBlobIdException;
import com.pdsu.scs.exception.web.es.InsertException;
import com.pdsu.scs.service.WebInformationService;
import com.pdsu.scs.utils.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private WebCommentMapper webCommentMapper;
	
	@Autowired
	private WebCommentReplyMapper webCommentReplyMapper;
	
	@Autowired
	private WebLabelControlMapper webLabelControlMapper;
	
	@Autowired
	private EsDao esDao;
	
	
	private String getDescriptionByWebData(String webdata) {
		Pattern p = Pattern.compile("[/#`~\r\n\t *]");
		Matcher m = p.matcher(webdata);
		String str = m.replaceAll("");
		return str.length() > 100 ? str.substring(0, 100) : str;
	}
	
	/*
	 * 插入一个网页信息
	 */
	@Override
	public int insert(@NonNull WebInformation information) throws InsertException {
		if(webInformationMapper.insertSelective(information) > 0) {
			EsBlobInformation blob = new EsBlobInformation(information.getId(), 
						getDescriptionByWebData(information.getWebDataString()), information.getTitle());
			if(esDao.insert(blob, information.getId())) {
				new Thread(()->{
					try {
						UserInformation user = userInformationMapper.selectUserByUid(information.getUid());
						Map<String, Object> map = esDao.queryByTableNameAndId("user", user.getId());
						EsUserInformation esuser = (EsUserInformation) SimpleUtils.
								getObjectByMapAndClass(map, EsUserInformation.class);
						esuser.setBlobnum(esuser.getBlobnum() + 1);
						esDao.update(esuser, user.getId());
					} catch (Exception e) {
					}
				}).start();
			}
			return information.getId();
		}
		return -1;
	}

	/*
	 * 删除一个网页信息
	 */
	@Override
	public boolean deleteById(@NonNull Integer id) throws NotFoundBlobIdException, DeleteInforException{
		if(!countByWebId(id)) {
			throw new NotFoundBlobIdException();
		}
		
		/**
		 * 删除文章对应的标签信息
		 */
		WebLabelControlExample webLabelControlExample = new WebLabelControlExample();
		com.pdsu.scs.bean.WebLabelControlExample.Criteria webLabelControlCriteria = webLabelControlExample.createCriteria();
		webLabelControlCriteria.andWidEqualTo(id);
		long webLabelControlCount = webLabelControlMapper.countByExample(webLabelControlExample);
		if(webLabelControlMapper.deleteByExample(webLabelControlExample) != webLabelControlCount) {
			throw new DeleteInforException("删除网页标签信息失败");
		}
		
		/**
		 * 删除网页相关的评论信息
		 */
		WebCommentReplyExample webCommentReplyExample = new WebCommentReplyExample();
		com.pdsu.scs.bean.WebCommentReplyExample.Criteria criteria = webCommentReplyExample.createCriteria();
		criteria.andWidEqualTo(id);
		long webCommentReplyCount = webCommentReplyMapper.countByExample(webCommentReplyExample);
		if(webCommentReplyMapper.deleteByExample(webCommentReplyExample) != webCommentReplyCount) {
			throw new DeleteInforException("删除网页评论信息失败");
		}
		
		WebCommentExample webCommentExample = new WebCommentExample();
		com.pdsu.scs.bean.WebCommentExample.Criteria webCommentCriteria = webCommentExample.createCriteria();
		webCommentCriteria.andWidEqualTo(id);
		long webCommentCount = webCommentMapper.countByExample(webCommentExample);
		if(webCommentMapper.deleteByExample(webCommentExample) != webCommentCount) {
			throw new DeleteInforException("删除网页评论信息失败");
		}
		
		/**
		 * 删除和网页相关的收藏信息 
		 */
		MyCollectionExample myCollectionExample = new MyCollectionExample();
		com.pdsu.scs.bean.MyCollectionExample.Criteria myCollectionCriteria1 = myCollectionExample.createCriteria();
		myCollectionCriteria1.andWidEqualTo(id);
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
		 *删除和网页相关的点赞信息
		 */
		WebThumbsExample webThumbsExample = new WebThumbsExample();
		com.pdsu.scs.bean.WebThumbsExample.Criteria webThumbsCriteria1 = webThumbsExample.createCriteria();
		webThumbsCriteria1.andWebidEqualTo(id);
		long webThumbsCount = webThumbsMapper.countByExample(webThumbsExample);
		if(webThumbsMapper.deleteByExample(webThumbsExample) != webThumbsCount) {
			throw new DeleteInforException("删除用户点赞信息失败");
		}
		WebInformation information = webInformationMapper.selectByPrimaryKey(id);
		int i = webInformationMapper.deleteByPrimaryKey(id);
		if(i >= 0) {
			new Thread(()->{
				try {
					UserInformation user = userInformationMapper.selectUserByUid(information.getUid());
					Map<String, Object> map = esDao.queryByTableNameAndId("user", user.getId());
					EsUserInformation esuser = (EsUserInformation) SimpleUtils.
							getObjectByMapAndClass(map, EsUserInformation.class);
					esuser.setBlobnum(esuser.getBlobnum()-1);
					esDao.update(esuser, user.getId());
				} catch (Exception e) {
				}
			}).start();
			return true;
		}else {
			throw new DeleteInforException("删除网页失败");
		}
	}

	@Override
	public WebInformation selectById(@NonNull Integer id) throws NotFoundBlobIdException{
		WebInformation key = webInformationMapper.selectByPrimaryKey(id);
		if(key == null) {
			throw new NotFoundBlobIdException();
		}
		return key;
	}

	@Override
	public List<WebInformation> selectWebInformationOrderByTimetest() {
		WebInformationExample example = new WebInformationExample();
		example.setOrderByClause("sub_time DESC");
		List<WebInformation> list = webInformationMapper.selectByExample(example);
		if(Objects.isNull(list)) {
			return new ArrayList<>();
		}
		return list;
	}

	@Override
	public List<WebInformation> selectWebInformationsByUid(@NonNull Integer uid) {
		WebInformationExample example = new WebInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		example.setOrderByClause("sub_time DESC");
		List<WebInformation> list = webInformationMapper.selectByExample(example);
		return list;
	}

	/*
	 *更新文章  
	 */
	@Override
	public boolean updateByWebId(@NonNull WebInformation web) {
		WebInformationExample example = new WebInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(web.getId());
		int i = webInformationMapper.updateByExampleSelective(web, example);
		if(i > 0) {
			new Thread(()->{
				try {
					Map<String, Object> map = esDao.queryByTableNameAndId("blob", web.getId());
					EsBlobInformation blob = (EsBlobInformation) SimpleUtils
							.getObjectByMapAndClass(map, EsBlobInformation.class);
					blob.setDescription(getDescriptionByWebData(web.getWebDataString()));
					System.out.println(blob);
					esDao.update(blob, blob.getWebid());
				} catch (Exception e) {
				}
			}).start();
		}
		return i > 0 ? true : false;
	}

	/**
	 * 查询博客是否存在
	 */
	@Override
	public boolean countByWebId(@NonNull Integer webid) {
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
	public int countByUid(@NonNull Integer uid) {
		UserInformationExample example = new UserInformationExample();
		com.pdsu.scs.bean.UserInformationExample.Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return (int) userInformationMapper.countByExample(example);
	}

	@Override
	public Integer countOriginalByUidAndContype(@NonNull Integer uid, @NonNull Integer contype) {
		WebInformationExample example = new WebInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andContypeEqualTo(contype);
		criteria.andUidEqualTo(uid);
		return (int) webInformationMapper.countByExample(example);
	}

	@Override
	public List<WebInformation> selectWebInformationsByIds(@Nullable List<Integer> webids, boolean flag) {
		if(Objects.isNull(webids) || webids.size() == 0) {
			return new ArrayList<>();
		}
		WebInformationExample example = new WebInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIn(webids);
		if(flag) {
			example.setOrderByClause("sub_time DESC");
		}
		return webInformationMapper.selectByExample(example);
	}

}

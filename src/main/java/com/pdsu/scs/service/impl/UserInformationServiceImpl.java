package com.pdsu.scs.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.EsUserInformation;
import com.pdsu.scs.bean.MyCollectionExample;
import com.pdsu.scs.bean.MyEmailExample;
import com.pdsu.scs.bean.MyImageExample;
import com.pdsu.scs.bean.MyLikeExample;
import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.bean.UserInformationExample;
import com.pdsu.scs.bean.UserInformationExample.Criteria;
import com.pdsu.scs.bean.VisitInformationExample;
import com.pdsu.scs.bean.WebCommentExample;
import com.pdsu.scs.bean.WebCommentReplyExample;
import com.pdsu.scs.bean.WebFileExample;
import com.pdsu.scs.bean.WebInformationExample;
import com.pdsu.scs.bean.WebThumbsExample;
import com.pdsu.scs.dao.MyCollectionMapper;
import com.pdsu.scs.dao.MyEmailMapper;
import com.pdsu.scs.dao.MyImageMapper;
import com.pdsu.scs.dao.MyLikeMapper;
import com.pdsu.scs.dao.UserInformationMapper;
import com.pdsu.scs.dao.VisitInformationMapper;
import com.pdsu.scs.dao.WebCommentMapper;
import com.pdsu.scs.dao.WebCommentReplyMapper;
import com.pdsu.scs.dao.WebFileMapper;
import com.pdsu.scs.dao.WebInformationMapper;
import com.pdsu.scs.dao.WebThumbsMapper;
import com.pdsu.scs.es.dao.EsDao;
import com.pdsu.scs.exception.web.DeleteInforException;
import com.pdsu.scs.exception.web.es.InsertException;
import com.pdsu.scs.exception.web.user.NotFoundUidException;
import com.pdsu.scs.exception.web.user.UidRepetitionException;
import com.pdsu.scs.service.UserInformationService;
import com.pdsu.scs.utils.HashUtils;
import com.pdsu.scs.utils.SimpleUtils;

/**
 * 该类继承 UserInformationService 接口, 用于处理与用户有关的逻辑
 * @author 半梦
 *
 */
@Service("userInformationService")
public class UserInformationServiceImpl implements UserInformationService {
	
	@Autowired
	private UserInformationMapper userInformationMapper;
	
	@Autowired
	private MyLikeMapper myLikeMapper;
	
	@Autowired
	private MyCollectionMapper myCollectionMapper;
	
	@Autowired
	private VisitInformationMapper visitInformationMapper;
	
	@Autowired
	private MyEmailMapper myEmailMapper;
	
	@Autowired
	private MyImageMapper myImageMapper;
	
	@Autowired
	private WebThumbsMapper webThumbsMapper;
	
	@Autowired
	private WebFileMapper webFileMapper;
	
	@Autowired
	private WebInformationMapper webInformationMapper;
	
	@Autowired
	private WebCommentMapper webCommentMapper;
	
	@Autowired
	private WebCommentReplyMapper webCommentReplyMapper;
	
	@Autowired
	private EsDao esDao;
	
	/**
	 * 插入一个用户信息
	 * @throws UidRepetitionException 
	 * @throws InsertException 
	 */
	@Override
	public boolean inset(UserInformation information) throws UidRepetitionException, InsertException {
		Integer uid = information.getUid();
		if(countByUid(uid) != 0) {
			throw new UidRepetitionException("学号不可重复");
		}
		String password = information.getPassword();
		password = HashUtils.getPasswordHash(uid, password);
		information.setPassword(password);
		if(userInformationMapper.insertSelective(information) > 0) {
			Integer id = information.getId();
			EsUserInformation user = new EsUserInformation(information.getUid(),
					0, information.getImgpath(), 0, information.getUsername());
			return esDao.insert(user, id);
		}
		return false;
	}

	/**
	 * 该方法不提供使用
	 * 删除一个用户信息
	 * 由于用户信息绑定了自身发布的博客页面
	 * 以及自身的访问量，头像，实名认证，邮箱绑定
	 * 上传文件，下载量， 收藏量，关注以及被关注等等，所以要先删除
	 * 所依赖于它的相关信息
	 * @throws NotFoundUidException, DeleteInforException
	 */
	@Override
	public boolean deleteByUid(Integer uid) throws NotFoundUidException, DeleteInforException {
		if(countByUid(uid) == 0) {
			throw new NotFoundUidException("该用户不存在");
		}
		
		/**
		 * 删除和用户相关的收藏信息 
		 */
		MyCollectionExample myCollectionExample = new MyCollectionExample();
		com.pdsu.scs.bean.MyCollectionExample.Criteria myCollectionCriteria1 = myCollectionExample.createCriteria();
		myCollectionCriteria1.andBidEqualTo(uid);
		com.pdsu.scs.bean.MyCollectionExample.Criteria myCollectionCriteria2 = myCollectionExample.createCriteria();
		myCollectionCriteria2.andUidEqualTo(uid);
		myCollectionExample.or(myCollectionCriteria2);
		//获取和用户相关的收藏信息总数
		long mycollectionCount = myCollectionMapper.countByExample(myCollectionExample);
		if(myCollectionMapper.deleteByExample(myCollectionExample) != mycollectionCount) {
			throw new DeleteInforException("删除用户收藏信息失败");
		}
		
		/**
		 *删除和用户相关的访问信息 
		 */
		VisitInformationExample visitInformationExample = new VisitInformationExample();
		com.pdsu.scs.bean.VisitInformationExample.Criteria visitInformationCriteria1 = visitInformationExample.createCriteria();
		visitInformationCriteria1.andSidEqualTo(uid);
		com.pdsu.scs.bean.VisitInformationExample.Criteria visitInformationCriteria2 = visitInformationExample.createCriteria();
		visitInformationCriteria2.andVidEqualTo(uid);
		visitInformationExample.or(visitInformationCriteria2);
		long visitCount = visitInformationMapper.countByExample(visitInformationExample);
		if(visitInformationMapper.deleteByExample(visitInformationExample) != visitCount) {
			throw new DeleteInforException("删除用户访问信息失败");
		}
		
		/**
		 *删除用户相关的点赞信息
		 */
		WebThumbsExample webThumbsExample = new WebThumbsExample();
		com.pdsu.scs.bean.WebThumbsExample.Criteria webThumbsCriteria1 = webThumbsExample.createCriteria();
		webThumbsCriteria1.andBidEqualTo(uid);
		com.pdsu.scs.bean.WebThumbsExample.Criteria webThumbsCriteria2 = webThumbsExample.createCriteria();
		webThumbsCriteria2.andUidEqualTo(uid);
		webThumbsExample.or(webThumbsCriteria2);
		long webThumbsCount = webThumbsMapper.countByExample(webThumbsExample);
		if(webThumbsMapper.deleteByExample(webThumbsExample) != webThumbsCount) {
			throw new DeleteInforException("删除用户点赞信息失败");
		}
		
		/**
		 *删除用户相关的邮箱信息 
		 */
		MyEmailExample myEmailExample = new MyEmailExample();
		com.pdsu.scs.bean.MyEmailExample.Criteria myEmailCriteria = myEmailExample.createCriteria();
		myEmailCriteria.andUidEqualTo(uid);
		long myEmailCount = myEmailMapper.countByExample(myEmailExample);
		if(myEmailMapper.deleteByExample(myEmailExample) != myEmailCount) {
			throw new DeleteInforException("删除用户邮箱失败");
		}
		
		/**
		 * 删除用户相关的头像信息
		 */
		MyImageExample myImageExample = new MyImageExample();
		com.pdsu.scs.bean.MyImageExample.Criteria myImageCriteria = myImageExample.createCriteria();
		myImageCriteria.andUidEqualTo(uid);
		long myImageCount = myImageMapper.countByExample(myImageExample);
		if(myImageMapper.deleteByExample(myImageExample) != myImageCount) {
			throw new DeleteInforException("删除用户头像失败");
		}
		
		/**
		 * 删除用户上传的文件信息
		 */
		WebFileExample webFileExample = new WebFileExample();
		com.pdsu.scs.bean.WebFileExample.Criteria webFileCriteria = webFileExample.createCriteria();
		webFileCriteria.andUidEqualTo(uid);
		long webFileCount = webFileMapper.countByExample(webFileExample);
		if(webFileMapper.deleteByExample(webFileExample) != webFileCount) {
			throw new DeleteInforException("删除用户文件失败");
		}
		
		/**
		 * 删除用户相关的关注信息
		 */
		MyLikeExample myLikeExample = new MyLikeExample();
		com.pdsu.scs.bean.MyLikeExample.Criteria myLikeCriteria1 = myLikeExample.createCriteria();
		myLikeCriteria1.andUidEqualTo(uid);
		com.pdsu.scs.bean.MyLikeExample.Criteria myLikeCriteria2 = myLikeExample.createCriteria();
		myLikeCriteria2.andLikeIdEqualTo(uid);
		myLikeExample.or(myLikeCriteria2);
		long myLikeCount = myLikeMapper.countByExample(myLikeExample);
		if(myLikeMapper.deleteByExample(myLikeExample) != myLikeCount) {
			throw new DeleteInforException("删除用户关注信息失败");
		}
		
		/**
		 * 删除用户相关的评论信息
		 */
		WebCommentReplyExample webCommentReplyExample = new WebCommentReplyExample();
		com.pdsu.scs.bean.WebCommentReplyExample.Criteria webCommentReplyCriteria1 = webCommentReplyExample.createCriteria();
		webCommentReplyCriteria1.andUidEqualTo(uid);
		com.pdsu.scs.bean.WebCommentReplyExample.Criteria webCommentReplyCriteria2 = webCommentReplyExample.createCriteria();
		webCommentReplyCriteria2.andBidEqualTo(uid);
		webCommentReplyExample.or(webCommentReplyCriteria2);
		long webCommentReplyCount = webCommentReplyMapper.countByExample(webCommentReplyExample);
		if(webCommentReplyMapper.deleteByExample(webCommentReplyExample) != webCommentReplyCount) {
			throw new DeleteInforException("删除用户评论信息失败");
		}
		
		WebCommentExample webCommentExample = new WebCommentExample();
		com.pdsu.scs.bean.WebCommentExample.Criteria webCommentCriteria = webCommentExample.createCriteria();
		webCommentCriteria.andUidEqualTo(uid);
		long webCommentCount = webCommentMapper.countByExample(webCommentExample);
		if(webCommentMapper.deleteByExample(webCommentExample) != webCommentCount) {
			throw new DeleteInforException("删除用户评论信息失败");
		}
		
		/**
		 * 删除用户博客相关的信息		
		 */
		WebInformationExample webInformationExample = new WebInformationExample();
		com.pdsu.scs.bean.WebInformationExample.Criteria webInforCriteria = webInformationExample.createCriteria();
		webInforCriteria.andUidEqualTo(uid);
		long webInforCount = webInformationMapper.countByExample(webInformationExample);
		if(webInformationMapper.deleteByExample(webInformationExample) != webInforCount) {
			throw new DeleteInforException("删除用户博客失败");
		}
		
		UserInformationExample example = new UserInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		if(userInformationMapper.deleteByExample(example) > 0) {
			return true;
		}
		throw new DeleteInforException("删除用户失败");
	}

	/**
	 * 根据用户的uid查询用户信息
	 */
	@Override
	public UserInformation selectByUid(Integer uid) {
		if(countByUid(uid) <= 0) {
			return null;
		}
		UserInformationExample example = new UserInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return userInformationMapper.selectByExample(example).get(0);
	}
	
	/**
	 * 根据一个用户的uid查询其所有关注人的信息
	 * @throws NotFoundUidException 
	 */
	@Override
	public List<UserInformation> selectUsersByUid(Integer uid) throws NotFoundUidException {
//		if(countByUid(uid) == 0) {
//			throw new NotFoundUidException("该用户不存在");
//		}
		List<Integer> likeids = myLikeMapper.selectLikeIdByUid(uid);
		if(likeids.size() == 0) {
			return new ArrayList<>();
		}
		UserInformationExample example = new UserInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidIn(likeids);
		return userInformationMapper.selectByExample(example);
	}
	
	/**
	 * 根据一个用户的 uid 查询一个用户所有粉丝的信息
	 * @throws NotFoundUidException 
	 */
	@Override
	public List<UserInformation> selectUsersByLikeId(Integer likeId) throws NotFoundUidException {
//		if(countByUid(likeId) == 0) {
//			throw new NotFoundUidException("该用户不存在");
//		}
		List<Integer> uids = myLikeMapper.selectUidByLikeId(likeId);
		if(uids.size() == 0) {
			return new ArrayList<UserInformation>();
		}
		UserInformationExample example = new UserInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidIn(uids);
		List<UserInformation> list = userInformationMapper.selectByExample(example);
		return list == null ? new ArrayList<UserInformation>() : list;
	}

	/**
	 * 根据用户的 uid 集群查询用户信息集群
	 * 如果该用户不存在, 则剔除集合
	 */
	@Override
	public List<UserInformation> selectUsersByUids(List<Integer> uids) {
		List<Integer> f = new ArrayList<Integer>();
		if(uids == null) {
			return new ArrayList<>();
		}
		for(Integer uid : uids) {
			if(countByUid(uid) == 0) {
				f.add(uid);
			}
		}
		uids.removeAll(f);
		if(uids.size() == 0) {
			return new ArrayList<>();
		}
		return userInformationMapper.selectUsersByUids(uids);
	}
	
	/**
	 * 查询是否有此账号
	 */
	@Override
	public int countByUid(Integer uid) {
		UserInformationExample example = new UserInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return (int) userInformationMapper.countByExample(example);
	}
	
	/**
	 * 修改密码
	 */
	@Override
	public boolean modifyThePassword(Integer uid, String password) {
		UserInformation user = selectByUid(uid);
		user.setPassword(HashUtils.getPasswordHash(uid, password));
		UserInformationExample example = new UserInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		int updateByExample = userInformationMapper.updateByExample(user, example);
		return updateByExample == 0 ? false : true;
	}

	@Override
	public int countByUserName(String username) {
		UserInformationExample example = new UserInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		return (int) userInformationMapper.countByExample(example);
	}

	@Override
	public boolean updateUserInformation(Integer uid, UserInformation user) throws NotFoundUidException {
		if(countByUid(uid) == 0) {
			throw new NotFoundUidException("该用户不存在");
		}
		UserInformationExample example = new UserInformationExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		boolean b = userInformationMapper.updateByExampleSelective(user, example) == 0 ? false : true;
		if(b) {
			new Thread(()->{
				try {
					UserInformation userinfor = userInformationMapper.selectUserByUid(uid);
					Map<String, Object> map = esDao.queryByTableNameAndId("user", userinfor.getId());
					EsUserInformation esuser = (EsUserInformation) SimpleUtils.
							getObjectByMapAndClass(map, EsUserInformation.class);
					esuser.setUsername(user.getUsername() == null ? userinfor.getUsername() : user.getUsername());
					esDao.update(esuser, user.getId());
				} catch (Exception e) {
				}
			}).start();
		}
		return b;
	}

	@Override
	public List<UserInformation> selectUserInformations() {
		return userInformationMapper.selectByExample(new UserInformationExample());
	}
}

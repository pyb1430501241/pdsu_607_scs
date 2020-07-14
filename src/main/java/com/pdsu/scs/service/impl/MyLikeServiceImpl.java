package com.pdsu.scs.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.EsUserInformation;
import com.pdsu.scs.bean.MyLike;
import com.pdsu.scs.bean.MyLikeExample;
import com.pdsu.scs.bean.UserInformationExample;
import com.pdsu.scs.bean.MyLikeExample.Criteria;
import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.dao.MyLikeMapper;
import com.pdsu.scs.dao.UserInformationMapper;
import com.pdsu.scs.es.dao.EsDao;
import com.pdsu.scs.exception.web.user.NotFoundUidAndLikeIdException;
import com.pdsu.scs.exception.web.user.NotFoundUidException;
import com.pdsu.scs.exception.web.user.UidAndLikeIdRepetitionException;
import com.pdsu.scs.service.MyLikeService;
import com.pdsu.scs.utils.SimpleUtils;

/**
 * 处理关注相关
 * @author 半梦
 *
 */
@Service("myLikeService")
public class MyLikeServiceImpl implements MyLikeService{

	@Autowired
	private MyLikeMapper myLikeMapper;
	
	@Autowired
	private UserInformationMapper userInformationMapper;
	
	@Autowired
	private EsDao esDao;
	
	@Override
	public long countByUid(Integer uid) throws NotFoundUidException {
		if(!isByUid(uid)) {
			throw new NotFoundUidException("该用户不存在");
		}
		MyLikeExample example = new MyLikeExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return myLikeMapper.countByExample(example);
	}

	@Override
	public long countByLikeId(Integer likeId) throws NotFoundUidException {
		if(!isByUid(likeId)) {
			throw new NotFoundUidException("该用户不存在");
		}
		MyLikeExample example = new MyLikeExample();
		Criteria criteria = example.createCriteria();
		criteria.andLikeIdEqualTo(likeId);
		return myLikeMapper.countByExample(example);
	}

	@Override
	public boolean insert(MyLike myLike) throws UidAndLikeIdRepetitionException {
		if(countByUidAndLikeId(myLike.getUid(), myLike.getLikeId())) {
			throw new UidAndLikeIdRepetitionException("你已关注此用户, 无法重复关注");
		}
		if(myLikeMapper.insertSelective(myLike) > 0) {
			new Thread(()->{
				try {
					UserInformation user = userInformationMapper.selectUserByUid(myLike.getLikeId());
					Map<String, Object> map = esDao.queryByTableNameAndId("user", user.getId());
					EsUserInformation esuser = (EsUserInformation) SimpleUtils.
							getObjectByMapAndClass(map, EsUserInformation.class);
					esuser.setLikenum(esuser.getLikenum()+1);
					esDao.update(esuser, user.getId());
				} catch (Exception e) {
				}
			}).start();
			return true;
		}
		return false;
	}
	
	@Override
	public List<Integer> selectLikeIdByUid(Integer uid) throws NotFoundUidException {
		if(!isByUid(uid)) {
			throw new NotFoundUidException("该用户不存在");
		}
		return myLikeMapper.selectLikeIdByUid(uid);
	}
	
	@Override
	public List<Integer> selectUidByLikeId(Integer likeId) throws NotFoundUidException {
		if(!isByUid(likeId)) {
			throw new NotFoundUidException("该用户不存在");
		}
		return myLikeMapper.selectUidByLikeId(likeId);
	}

	@Override
	public boolean isByUid(Integer uid) {
		UserInformationExample example = new UserInformationExample();
		com.pdsu.scs.bean.UserInformationExample.Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return userInformationMapper.countByExample(example) > 0 ? true : false;
	}

	@Override
	public boolean countByUidAndLikeId(Integer uid, Integer likeId) {
		MyLikeExample example = new MyLikeExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		criteria.andLikeIdEqualTo(likeId);
		return myLikeMapper.countByExample(example) > 0 ? true : false;
	}
	
	@Override
	public boolean deleteByLikeIdAndUid(Integer likeId, Integer uid) throws NotFoundUidAndLikeIdException {
		if(!countByUidAndLikeId(uid, likeId)) {
			throw new NotFoundUidAndLikeIdException("您并未关注该用户");
		}
		MyLikeExample example = new MyLikeExample();
		Criteria criteria = example.createCriteria();
		criteria.andLikeIdEqualTo(uid);
		criteria.andUidEqualTo(likeId);
		boolean b = myLikeMapper.deleteByExample(example) > 0 ? true : false;
		if(b) {
			new Thread(()->{
				try {
					UserInformation user = userInformationMapper.selectUserByUid(uid);
					Map<String, Object> map = esDao.queryByTableNameAndId("user", user.getId());
					EsUserInformation esuser = (EsUserInformation) SimpleUtils.
							getObjectByMapAndClass(map, EsUserInformation.class);
					esuser.setLikenum(esuser.getLikenum()-1);
					esDao.update(esuser, user.getId());
				} catch (Exception e) {
				}
			}).start();
		}
		return b;
	}
}

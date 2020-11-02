package com.pdsu.scs.service.impl;

import com.pdsu.scs.bean.*;
import com.pdsu.scs.bean.MyLikeExample.Criteria;
import com.pdsu.scs.dao.MyLikeMapper;
import com.pdsu.scs.dao.UserInformationMapper;
import com.pdsu.scs.es.dao.EsDao;
import com.pdsu.scs.exception.web.user.NotFoundUidAndLikeIdException;
import com.pdsu.scs.exception.web.user.NotFoundUidException;
import com.pdsu.scs.exception.web.user.UidAndLikeIdRepetitionException;
import com.pdsu.scs.service.MyLikeService;
import com.pdsu.scs.utils.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 处理关注相关
 * @author 半梦
 *
 */
@Service("myLikeService")
public class MyLikeServiceImpl implements MyLikeService {

	@Autowired
	private MyLikeMapper myLikeMapper;
	
	@Autowired
	private UserInformationMapper userInformationMapper;
	
	@Autowired
	private EsDao esDao;
	
	@Override
	public long countByUid(@NonNull Integer uid) throws NotFoundUidException {
		if(!isByUid(uid)) {
			throw new NotFoundUidException("该用户不存在");
		}
		MyLikeExample example = new MyLikeExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return myLikeMapper.countByExample(example);
	}

	@Override
	public long countByLikeId(@NonNull Integer likeId) throws NotFoundUidException {
		if(!isByUid(likeId)) {
			throw new NotFoundUidException("该用户不存在");
		}
		MyLikeExample example = new MyLikeExample();
		Criteria criteria = example.createCriteria();
		criteria.andLikeIdEqualTo(likeId);
		return myLikeMapper.countByExample(example);
	}

	@Override
	public boolean insert(@NonNull MyLike myLike) throws UidAndLikeIdRepetitionException {
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
	public List<Integer> selectLikeIdByUid(@NonNull Integer uid) throws NotFoundUidException {
		if(!isByUid(uid)) {
			throw new NotFoundUidException("该用户不存在");
		}
		return myLikeMapper.selectLikeIdByUid(uid);
	}
	
	@Override
	public List<Integer> selectUidByLikeId(@NonNull Integer likeId) throws NotFoundUidException {
		if(!isByUid(likeId)) {
			throw new NotFoundUidException("该用户不存在");
		}
		return myLikeMapper.selectUidByLikeId(likeId);
	}

	@Override
	public boolean isByUid(@NonNull Integer uid) {
		UserInformationExample example = new UserInformationExample();
		com.pdsu.scs.bean.UserInformationExample.Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return userInformationMapper.countByExample(example) > 0;
	}

	@Override
	public boolean countByUidAndLikeId(@NonNull Integer uid, @NonNull Integer likeId) {
		MyLikeExample example = new MyLikeExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		criteria.andLikeIdEqualTo(likeId);
		return myLikeMapper.countByExample(example) > 0 ? true : false;
	}
	
	@Override
	public boolean deleteByLikeIdAndUid(@NonNull Integer likeId, @NonNull Integer uid) throws NotFoundUidAndLikeIdException {
		if(!countByUidAndLikeId(likeId, uid)) {
			throw new NotFoundUidAndLikeIdException("您并未关注该用户");
		}
		MyLikeExample example = new MyLikeExample();
		Criteria criteria = example.createCriteria();
		criteria.andLikeIdEqualTo(uid);
		criteria.andUidEqualTo(likeId);
		boolean b = myLikeMapper.deleteByExample(example) > 0;
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

	@Override
	public List<Boolean> countByUidAndLikeId(@NonNull Integer uid, @NonNull List<Integer> uids) throws NotFoundUidException {
		if(!isByUid(uid)) {
			throw new NotFoundUidException("该用户不存在");
		}
		List<Boolean> islikes = new ArrayList<>();
		for(Integer likeId : uids) {
			islikes.add(countByUidAndLikeId(uid, likeId));
		}
		return islikes;
	}
	
	
}

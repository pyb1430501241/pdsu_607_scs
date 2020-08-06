package com.pdsu.scs.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.EsUserInformation;
import com.pdsu.scs.bean.MyImage;
import com.pdsu.scs.bean.MyImageExample;
import com.pdsu.scs.bean.MyImageExample.Criteria;
import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.bean.UserInformationExample;
import com.pdsu.scs.dao.MyImageMapper;
import com.pdsu.scs.dao.UserInformationMapper;
import com.pdsu.scs.es.dao.EsDao;
import com.pdsu.scs.exception.web.user.NotFoundUidException;
import com.pdsu.scs.service.MyImageService;
import com.pdsu.scs.utils.SimpleUtils;

/**
 * 
 * @author 半梦
 *
 */
@Service("myImageService")
public class MyImageServiceImpl implements MyImageService {

	@Autowired
	private MyImageMapper myImageMapper;
	
	@Autowired
	private UserInformationMapper userInformationMapper;
	
	@Autowired
	private EsDao esDao;
	
	@Override
	public List<MyImage> selectImagePathByUids(List<Integer> uids) {
		List<Integer> ids = new ArrayList<Integer>();
		for(Integer id : uids) {
			if(countByUid(id)) {
				ids.add(id);
			}
		}
		if(ids.size() == 0) {
			return new ArrayList<MyImage>();
		}
		MyImageExample example = new MyImageExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidIn(ids);
		List<MyImage> list = myImageMapper.selectByExample(example);
		return list;
	}

	@Override
	public MyImage selectImagePathByUid(Integer uid) throws NotFoundUidException {
		if(!countByUid(uid)) {
			throw new NotFoundUidException("该用户不存在");
		}
		MyImageExample example = new MyImageExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return myImageMapper.selectByExample(example).size() == 0 ? new MyImage(uid, "01.png") : myImageMapper.selectByExample(example).get(0);
	}
	
	/**
	 * 查询是否有此账号
	 */
	@Override
	public boolean countByUid(Integer uid) {
		UserInformationExample example = new UserInformationExample();
		com.pdsu.scs.bean.UserInformationExample.Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid);
		return userInformationMapper.countByExample(example) > 0 ? true : false;
	}

	@Override
	public boolean insert(MyImage myImage) throws NotFoundUidException {
		if(!countByUid(myImage.getUid())) {
			throw new NotFoundUidException("该用户不存在");
		}
		return myImageMapper.insertSelective(myImage) == 0 ? false : true;
	}

	@Override
	public boolean update(MyImage myImage) throws NotFoundUidException {
		if(!countByUid(myImage.getUid())) {
			throw new NotFoundUidException("该用户不存在");
		}
		MyImageExample example = new MyImageExample();
		Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(myImage.getUid());
		boolean b = myImageMapper.updateByExampleSelective(myImage, example) == 0 ? false : true;
		if(b) {
			new Thread(()->{
				try {
					UserInformation user = userInformationMapper.selectUserByUid(myImage.getUid());
					Map<String, Object> map = esDao.queryByTableNameAndId("user", user.getId());
					EsUserInformation esuser = (EsUserInformation) SimpleUtils.
							getObjectByMapAndClass(map, EsUserInformation.class);
					esuser.setImgpath(myImage.getImagePath());
					esDao.update(esuser, user.getId());
				} catch (Exception e) {
				}
			}).start();
		}
		return b;
	}
}

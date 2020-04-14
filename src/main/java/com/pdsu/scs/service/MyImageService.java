package com.pdsu.scs.service;

import java.util.List;

import com.pdsu.scs.bean.MyImage;

/**
 * 该接口用于提供与头像相关的方法
 * @author Admin
 *
 */
public interface MyImageService {
	
	//根据一组学号来获取一组头像地址
	public List<MyImage> selectImagePathByUids(List<Integer> uids);

	//根据学号来获取头像地址
	public MyImage selectImagePathByUid(Integer uid);
	
}

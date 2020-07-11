package com.pdsu.scs.service;

import java.util.List;

import com.pdsu.scs.bean.VisitInformation;
import com.pdsu.scs.exception.web.blob.NotFoundBlobIdException;
import com.pdsu.scs.exception.web.es.UpdateException;
import com.pdsu.scs.exception.web.user.NotFoundUidException;

/**
 * 该接口提供访问相关的方法
 * @author 半梦
 *
 */
public interface VisitInformationService {
	
	//根据网页id集获取该网页的访问量集
	public List<Integer> selectVisitsByWebIds(List<Integer> webids);
	
	//根据一个人的uid来获取其总访问量
	public Integer selectVisitsByVid(Integer id) throws NotFoundUidException;
	
	//插入一条记录
	public boolean insert(VisitInformation visit);
	
	//根据网页id获取该网页访问量
	public Integer selectvisitByWebId(Integer webid) throws NotFoundBlobIdException;
	
	//查询页面是否存在
	public boolean countByWebId(Integer webid);
	
	//查询用户是否存在
	public boolean countByUid(Integer uid);
}

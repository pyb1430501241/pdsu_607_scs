package com.pdsu.scs.service;

import com.pdsu.scs.bean.AccountStatusExample;
import org.springframework.lang.NonNull;

/**
 * 该接口提供与账号状态相关的方法
 * @author 半梦
 *
 */
public interface AcconutStatusService {
	
	//获取数据总数
	public long countByExample(AccountStatusExample example);
	
	//根据 id 删除记录
	public boolean deleteById(@NonNull Integer id);
	
}

package com.pdsu.scs.service;

import com.pdsu.scs.bean.AccountStatusExample;

public interface AcconutStatusSercice {
	
	//获取数据总数
	public long countByExample(AccountStatusExample example);
	
	//根据 id 删除记录
	public boolean deleteById(Integer id);
	
}

package com.pdsu.scs.service;

import com.pdsu.scs.bean.PenaltyRecord;

public interface PenaltyRecordService {
	
	//根据学号获取处罚信息
	public PenaltyRecord selectPenaltyRecordByUid(Integer uid);
	
}

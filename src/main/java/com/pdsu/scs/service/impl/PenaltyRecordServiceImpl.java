package com.pdsu.scs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.pdsu.scs.bean.PenaltyRecord;
import com.pdsu.scs.dao.PenaltyRecordMapper;
import com.pdsu.scs.service.PenaltyRecordService;

public class PenaltyRecordServiceImpl implements PenaltyRecordService {

	@Autowired
	private PenaltyRecordMapper penaltyRecordMapper;
	
	@Override
	public PenaltyRecord selectPenaltyRecordByUid(Integer uid) {
		return penaltyRecordMapper.selectPenaltyRecordByUid(uid);
	}

}

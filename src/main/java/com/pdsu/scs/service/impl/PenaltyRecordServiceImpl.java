package com.pdsu.scs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.PenaltyRecord;
import com.pdsu.scs.dao.PenaltyRecordMapper;
import com.pdsu.scs.service.PenaltyRecordService;

/**
 * 
 * @author 半梦
 *
 */
@Service("penaltyRecordService")
public class PenaltyRecordServiceImpl implements PenaltyRecordService {

	@Autowired
	private PenaltyRecordMapper penaltyRecordMapper;
	
	@Override
	public PenaltyRecord selectPenaltyRecordByUid(@NonNull Integer uid) {
		return penaltyRecordMapper.selectPenaltyRecordByUid(uid);
	}

}

package com.pdsu.scs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.Contype;
import com.pdsu.scs.bean.ContypeExample;
import com.pdsu.scs.dao.ContypeMapper;
import com.pdsu.scs.service.ContypeService;

/**
 * @author 半梦
 */
@Service("contypeService")
public class ContypeServiceImpl implements ContypeService {

	@Autowired
	private ContypeMapper contypeMapper;
	
	@Override
	public List<Contype> selectContypes() {
		return contypeMapper.selectByExample(new ContypeExample());
	}

}

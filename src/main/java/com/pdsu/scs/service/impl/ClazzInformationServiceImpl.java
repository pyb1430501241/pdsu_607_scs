package com.pdsu.scs.service.impl;

import com.pdsu.scs.bean.ClazzInformation;
import com.pdsu.scs.dao.ClazzInformationMapper;
import com.pdsu.scs.service.ClazzInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * @author 半梦
 * @create 2020-08-21 14:47
 */
@Service("clazzInformationService")
@Deprecated
public class ClazzInformationServiceImpl implements ClazzInformationService {

    @Autowired
    private ClazzInformationMapper clazzInformationMapper;

    @Override
    public boolean insert(@NonNull ClazzInformation clazzInformation) {
        int i = clazzInformationMapper.insertSelective(clazzInformation);
        return i > 0;
    }
}

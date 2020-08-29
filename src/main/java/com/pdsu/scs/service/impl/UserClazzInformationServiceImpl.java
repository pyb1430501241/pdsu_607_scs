package com.pdsu.scs.service.impl;

import com.pdsu.scs.dao.UserClazzInformationMapper;
import com.pdsu.scs.service.UserClazzInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 半梦
 * @create 2020-08-21 18:30
 */
@Service("userClazzInformationService")
public class UserClazzInformationServiceImpl implements UserClazzInformationService {

    @Autowired
    private UserClazzInformationMapper userClazzInformationMapper;

    @Override
    public boolean insertByList(List<Integer> uids, Integer clazzId) {
        return userClazzInformationMapper.insertByList(uids, clazzId) > 0;
    }
}

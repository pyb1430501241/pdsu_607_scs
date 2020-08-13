package com.pdsu.scs.service.impl;

import com.pdsu.scs.bean.UserRole;
import com.pdsu.scs.bean.UserRoleExample;
import com.pdsu.scs.dao.UserRoleMapper;
import com.pdsu.scs.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 半梦
 * @create 2020-08-13 16:10
 */
@Service("userRoleService")
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public boolean isAdmin(Integer uid) {
        UserRoleExample example = new UserRoleExample();
        UserRoleExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(uid);
        criteria.andRoleidEqualTo(3);
        return userRoleMapper.countByExample(example) != 0;
    }

    @Override
    public boolean isTeacher(Integer uid) {
        UserRoleExample example = new UserRoleExample();
        UserRoleExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(uid);
        criteria.andRoleidEqualTo(2);
        return userRoleMapper.countByExample(example) != 0;
    }

    @Override
    public boolean insert(UserRole userRole) {
        return userRoleMapper.insertSelective(userRole) != 0;
    }
}

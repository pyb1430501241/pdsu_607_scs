package com.pdsu.scs.service;

import com.pdsu.scs.bean.UserRole;

/**
 * @author 半梦
 * @create 2020-08-13 16:09
 */
public interface UserRoleService {

    /**
     * 判断用户是否为管理员
     * @param uid
     * @return
     */
    public boolean isAdmin(Integer uid);

    /**
     * 判断用户是否为老师
     * @param uid
     * @return
     */
    public boolean isTeacher(Integer uid);

    /**
     * 权限分配
     * @param userRole
     * @return
     */
    public boolean insert(UserRole userRole);
}

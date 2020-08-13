package com.pdsu.scs.service;

import com.pdsu.scs.bean.SystemNotification;

import java.util.List;

/**
 * @author 半梦
 * @create 2020-08-13 14:09
 */
public interface SystemNotificationService {

    /**
     * 插入系统通知
     * @param list
     * @return
     */
    public boolean insert(List<SystemNotification> list);

    /**
     * 获取用户通知
     * @param uid
     * @return
     */
    public List<SystemNotification> selectSystemNotificationsByUid(Integer uid);

    /**
     * 删除用户通知
     * @param uid
     * @return
     */
    public boolean deleteSystemNotificationsByUid(Integer uid);

    /**
     * 更新通知
     * @param uid
     * @return
     */
    public boolean updateSystemNotificationsByUid(Integer uid);

    /**
     * 获取未读信息数量
     * @param uid
     * @return
     */
    public Integer countSystemNotificationByUidAndUnRead(Integer uid);
}

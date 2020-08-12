package com.pdsu.scs.service;

import com.pdsu.scs.bean.UserBrowsingRecord;

import java.util.List;

/**
 * @author 半梦
 * @create 2020-08-12 22:41
 */
public interface UserBrowsingRecordService {

    /**
     * 添加浏览记录
     * @param userBrowsingRecord
     * @return
     */
    public boolean insert(UserBrowsingRecord userBrowsingRecord);

    /**
     * 获取用户浏览记录
     * @param uid
     * @return
     */
    public List<UserBrowsingRecord> selectBrowsingRecordByUid(Integer uid);

    /**
     * 清空用户记录
     * @param uid
     * @return
     */
    public boolean deleteBrowsingRecordByUid(Integer uid);
}

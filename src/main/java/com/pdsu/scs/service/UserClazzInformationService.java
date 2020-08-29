package com.pdsu.scs.service;

import java.util.List;

/**
 * @author 半梦
 * @create 2020-08-21 18:29
 */
public interface UserClazzInformationService {

    /**
     * 批量插入学生
     * @param uids
     * @return
     */
    public boolean insertByList (List<Integer> uids, Integer clazzId);

}

package com.pdsu.scs.service;

import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author 半梦
 * @create 2020-08-21 18:29
 */
@Deprecated
public interface UserClazzInformationService {

    /**
     * 批量插入学生
     * @param uids
     * @return
     */
    @Deprecated
    public boolean insertByList (@NonNull List<Integer> uids, @NonNull Integer clazzId);

}

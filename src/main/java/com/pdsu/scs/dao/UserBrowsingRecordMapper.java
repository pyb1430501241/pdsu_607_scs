package com.pdsu.scs.dao;

import com.pdsu.scs.bean.UserBrowsingRecord;
import com.pdsu.scs.bean.UserBrowsingRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserBrowsingRecordMapper {
    long countByExample(UserBrowsingRecordExample example);

    int deleteByExample(UserBrowsingRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserBrowsingRecord record);

    int insertSelective(UserBrowsingRecord record);

    List<UserBrowsingRecord> selectByExample(UserBrowsingRecordExample example);

    UserBrowsingRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserBrowsingRecord record, @Param("example") UserBrowsingRecordExample example);

    int updateByExample(@Param("record") UserBrowsingRecord record, @Param("example") UserBrowsingRecordExample example);

    int updateByPrimaryKeySelective(UserBrowsingRecord record);

    int updateByPrimaryKey(UserBrowsingRecord record);
}
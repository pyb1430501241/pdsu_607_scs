package com.pdsu.scs.dao;

import com.pdsu.scs.bean.SystemNotification;
import com.pdsu.scs.bean.SystemNotificationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SystemNotificationMapper {
    long countByExample(SystemNotificationExample example);

    int deleteByExample(SystemNotificationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SystemNotification record);

    int insertSelective(SystemNotification record);

    List<SystemNotification> selectByExample(SystemNotificationExample example);

    SystemNotification selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SystemNotification record, @Param("example") SystemNotificationExample example);

    int updateByExample(@Param("record") SystemNotification record, @Param("example") SystemNotificationExample example);

    int updateByPrimaryKeySelective(SystemNotification record);

    int updateByPrimaryKey(SystemNotification record);

    int insertByList(@Param("list") List<SystemNotification> list);
}
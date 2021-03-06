package com.pdsu.scs.dao;

import com.pdsu.scs.bean.AccountStatus;
import com.pdsu.scs.bean.AccountStatusExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AccountStatusMapper {
    long countByExample(AccountStatusExample example);

    int deleteByExample(AccountStatusExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AccountStatus record);

    int insertSelective(AccountStatus record);

    List<AccountStatus> selectByExample(AccountStatusExample example);

    AccountStatus selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AccountStatus record, @Param("example") AccountStatusExample example);

    int updateByExample(@Param("record") AccountStatus record, @Param("example") AccountStatusExample example);

    int updateByPrimaryKeySelective(AccountStatus record);

    int updateByPrimaryKey(AccountStatus record);
}
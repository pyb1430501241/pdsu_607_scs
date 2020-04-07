package com.pdsu.scs.dao;

import com.pdsu.scs.bean.MyEmail;
import com.pdsu.scs.bean.MyEmailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MyEmailMapper {
    long countByExample(MyEmailExample example);

    int deleteByExample(MyEmailExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MyEmail record);

    int insertSelective(MyEmail record);

    List<MyEmail> selectByExample(MyEmailExample example);

    MyEmail selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MyEmail record, @Param("example") MyEmailExample example);

    int updateByExample(@Param("record") MyEmail record, @Param("example") MyEmailExample example);

    int updateByPrimaryKeySelective(MyEmail record);

    int updateByPrimaryKey(MyEmail record);
}
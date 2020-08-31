package com.pdsu.scs.dao;

import com.pdsu.scs.bean.Contype;
import com.pdsu.scs.bean.ContypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ContypeMapper {
    long countByExample(ContypeExample example);

    int deleteByExample(ContypeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Contype record);

    int insertSelective(Contype record);

    List<Contype> selectByExample(ContypeExample example);

    Contype selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Contype record, @Param("example") ContypeExample example);

    int updateByExample(@Param("record") Contype record, @Param("example") ContypeExample example);

    int updateByPrimaryKeySelective(Contype record);

    int updateByPrimaryKey(Contype record);
}
package com.pdsu.scs.dao;

import com.pdsu.scs.bean.RealName;
import com.pdsu.scs.bean.RealNameExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RealNameMapper {
    long countByExample(RealNameExample example);

    int deleteByExample(RealNameExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RealName record);

    int insertSelective(RealName record);

    List<RealName> selectByExample(RealNameExample example);

    RealName selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RealName record, @Param("example") RealNameExample example);

    int updateByExample(@Param("record") RealName record, @Param("example") RealNameExample example);

    int updateByPrimaryKeySelective(RealName record);

    int updateByPrimaryKey(RealName record);
}
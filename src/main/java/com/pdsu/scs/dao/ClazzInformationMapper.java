package com.pdsu.scs.dao;

import com.pdsu.scs.bean.ClazzInformation;
import com.pdsu.scs.bean.ClazzInformationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ClazzInformationMapper {
    long countByExample(ClazzInformationExample example);

    int deleteByExample(ClazzInformationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ClazzInformation record);

    int insertSelective(ClazzInformation record);

    List<ClazzInformation> selectByExample(ClazzInformationExample example);

    ClazzInformation selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ClazzInformation record, @Param("example") ClazzInformationExample example);

    int updateByExample(@Param("record") ClazzInformation record, @Param("example") ClazzInformationExample example);

    int updateByPrimaryKeySelective(ClazzInformation record);

    int updateByPrimaryKey(ClazzInformation record);
}
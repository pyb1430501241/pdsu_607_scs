package com.pdsu.scs.dao;

import com.pdsu.scs.bean.VisitInformation;
import com.pdsu.scs.bean.VisitInformationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VisitInformationMapper {
    long countByExample(VisitInformationExample example);

    int deleteByExample(VisitInformationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(VisitInformation record);

    int insertSelective(VisitInformation record);

    List<VisitInformation> selectByExample(VisitInformationExample example);

    VisitInformation selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") VisitInformation record, @Param("example") VisitInformationExample example);

    int updateByExample(@Param("record") VisitInformation record, @Param("example") VisitInformationExample example);

    int updateByPrimaryKeySelective(VisitInformation record);

    int updateByPrimaryKey(VisitInformation record);
}
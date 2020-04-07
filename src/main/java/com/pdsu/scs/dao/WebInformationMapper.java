package com.pdsu.scs.dao;

import com.pdsu.scs.bean.WebInformation;
import com.pdsu.scs.bean.WebInformationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WebInformationMapper {
    long countByExample(WebInformationExample example);

    int deleteByExample(WebInformationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WebInformation record);

    int insertSelective(WebInformation record);

    List<WebInformation> selectByExampleWithBLOBs(WebInformationExample example);

    List<WebInformation> selectByExample(WebInformationExample example);

    WebInformation selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WebInformation record, @Param("example") WebInformationExample example);

    int updateByExampleWithBLOBs(@Param("record") WebInformation record, @Param("example") WebInformationExample example);

    int updateByExample(@Param("record") WebInformation record, @Param("example") WebInformationExample example);

    int updateByPrimaryKeySelective(WebInformation record);

    int updateByPrimaryKeyWithBLOBs(WebInformation record);

    int updateByPrimaryKey(WebInformation record);
}
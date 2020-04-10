package com.pdsu.scs.dao;

import com.pdsu.scs.bean.WebHeat;
import com.pdsu.scs.bean.WebHeatExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WebHeatMapper {
    long countByExample(WebHeatExample example);

    int deleteByExample(WebHeatExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WebHeat record);

    int insertSelective(WebHeat record);

    List<WebHeat> selectByExample(WebHeatExample example);

    WebHeat selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WebHeat record, @Param("example") WebHeatExample example);

    int updateByExample(@Param("record") WebHeat record, @Param("example") WebHeatExample example);

    int updateByPrimaryKeySelective(WebHeat record);

    int updateByPrimaryKey(WebHeat record);
}
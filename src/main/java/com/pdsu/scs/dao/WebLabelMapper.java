package com.pdsu.scs.dao;

import com.pdsu.scs.bean.WebLabel;
import com.pdsu.scs.bean.WebLabelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WebLabelMapper {
    long countByExample(WebLabelExample example);

    int deleteByExample(WebLabelExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WebLabel record);

    int insertSelective(WebLabel record);

    List<WebLabel> selectByExample(WebLabelExample example);

    WebLabel selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WebLabel record, @Param("example") WebLabelExample example);

    int updateByExample(@Param("record") WebLabel record, @Param("example") WebLabelExample example);

    int updateByPrimaryKeySelective(WebLabel record);

    int updateByPrimaryKey(WebLabel record);
}
package com.pdsu.scs.dao;

import com.pdsu.scs.bean.WebLabelControl;
import com.pdsu.scs.bean.WebLabelControlExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WebLabelControlMapper {
    long countByExample(WebLabelControlExample example);

    int deleteByExample(WebLabelControlExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WebLabelControl record);

    int insertSelective(WebLabelControl record);

    List<WebLabelControl> selectByExample(WebLabelControlExample example);

    WebLabelControl selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WebLabelControl record, @Param("example") WebLabelControlExample example);

    int updateByExample(@Param("record") WebLabelControl record, @Param("example") WebLabelControlExample example);

    int updateByPrimaryKeySelective(WebLabelControl record);

    int updateByPrimaryKey(WebLabelControl record);

}
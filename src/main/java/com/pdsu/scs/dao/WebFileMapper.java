package com.pdsu.scs.dao;

import com.pdsu.scs.bean.WebFile;
import com.pdsu.scs.bean.WebFileExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WebFileMapper {
    long countByExample(WebFileExample example);

    int deleteByExample(WebFileExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WebFile record);

    int insertSelective(WebFile record);

    List<WebFile> selectByExample(WebFileExample example);

    WebFile selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WebFile record, @Param("example") WebFileExample example);

    int updateByExample(@Param("record") WebFile record, @Param("example") WebFileExample example);

    int updateByPrimaryKeySelective(WebFile record);

    int updateByPrimaryKey(WebFile record);
}
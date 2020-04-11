package com.pdsu.scs.dao;

import com.pdsu.scs.bean.WebThumbs;
import com.pdsu.scs.bean.WebThumbsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WebThumbsMapper {

    long countByExample(WebThumbsExample example);
	
    int deleteByExample(WebThumbsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WebThumbs record);

    int insertSelective(WebThumbs record);

    List<WebThumbs> selectByExample(WebThumbsExample example);

    WebThumbs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WebThumbs record, @Param("example") WebThumbsExample example);

    int updateByExample(@Param("record") WebThumbs record, @Param("example") WebThumbsExample example);

    int updateByPrimaryKeySelective(WebThumbs record);

    int updateByPrimaryKey(WebThumbs record);

}
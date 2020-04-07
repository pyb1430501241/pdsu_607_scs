package com.pdsu.scs.dao;

import com.pdsu.scs.bean.MyImage;
import com.pdsu.scs.bean.MyImageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MyImageMapper {
    long countByExample(MyImageExample example);

    int deleteByExample(MyImageExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MyImage record);

    int insertSelective(MyImage record);

    List<MyImage> selectByExample(MyImageExample example);

    MyImage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MyImage record, @Param("example") MyImageExample example);

    int updateByExample(@Param("record") MyImage record, @Param("example") MyImageExample example);

    int updateByPrimaryKeySelective(MyImage record);

    int updateByPrimaryKey(MyImage record);
}
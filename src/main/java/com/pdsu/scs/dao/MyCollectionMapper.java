package com.pdsu.scs.dao;

import com.pdsu.scs.bean.MyCollection;
import com.pdsu.scs.bean.MyCollectionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MyCollectionMapper {
    long countByExample(MyCollectionExample example);

    int deleteByExample(MyCollectionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MyCollection record);

    int insertSelective(MyCollection record);

    List<MyCollection> selectByExample(MyCollectionExample example);

    MyCollection selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MyCollection record, @Param("example") MyCollectionExample example);

    int updateByExample(@Param("record") MyCollection record, @Param("example") MyCollectionExample example);

    int updateByPrimaryKeySelective(MyCollection record);

    int updateByPrimaryKey(MyCollection record);
}
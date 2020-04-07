package com.pdsu.scs.dao;

import com.pdsu.scs.bean.PenaltyRecord;
import com.pdsu.scs.bean.PenaltyRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PenaltyRecordMapper {
    long countByExample(PenaltyRecordExample example);

    int deleteByExample(PenaltyRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PenaltyRecord record);

    int insertSelective(PenaltyRecord record);

    List<PenaltyRecord> selectByExample(PenaltyRecordExample example);

    PenaltyRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PenaltyRecord record, @Param("example") PenaltyRecordExample example);

    int updateByExample(@Param("record") PenaltyRecord record, @Param("example") PenaltyRecordExample example);

    int updateByPrimaryKeySelective(PenaltyRecord record);

    int updateByPrimaryKey(PenaltyRecord record);
}
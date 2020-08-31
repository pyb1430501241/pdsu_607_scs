package com.pdsu.scs.dao;

import com.pdsu.scs.bean.FileDownload;
import com.pdsu.scs.bean.FileDownloadExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FileDownloadMapper {
    long countByExample(FileDownloadExample example);

    int deleteByExample(FileDownloadExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FileDownload record);

    int insertSelective(FileDownload record);

    List<FileDownload> selectByExample(FileDownloadExample example);

    FileDownload selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FileDownload record, @Param("example") FileDownloadExample example);

    int updateByExample(@Param("record") FileDownload record, @Param("example") FileDownloadExample example);

    int updateByPrimaryKeySelective(FileDownload record);

    int updateByPrimaryKey(FileDownload record);
}
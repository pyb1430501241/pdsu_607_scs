package com.pdsu.scs.dao;

import com.pdsu.scs.bean.WebCommentReply;
import com.pdsu.scs.bean.WebCommentReplyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WebCommentReplyMapper {
    long countByExample(WebCommentReplyExample example);

    int deleteByExample(WebCommentReplyExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WebCommentReply record);

    int insertSelective(WebCommentReply record);

    List<WebCommentReply> selectByExample(WebCommentReplyExample example);

    WebCommentReply selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WebCommentReply record, @Param("example") WebCommentReplyExample example);

    int updateByExample(@Param("record") WebCommentReply record, @Param("example") WebCommentReplyExample example);

    int updateByPrimaryKeySelective(WebCommentReply record);

    int updateByPrimaryKey(WebCommentReply record);
}
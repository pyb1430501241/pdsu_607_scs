package com.pdsu.scs.utils;

import org.springframework.lang.NonNull;

/**
 * @author 半梦
 * @create 2020-11-02 17:27
 */
public class StringFactory {

    /**
     *  获取用户的文章被评论时的系统提示信息
     * @param userName 用户名
     * @param title 文章名
     * @param comment 评论信息
     * @return
     *  完整的系统提示信息
     */
    @NonNull
    public static String getCommentString(@NonNull String userName, @NonNull String title,
                                          @NonNull String comment) {
        StringBuilder b = new StringBuilder();
        b.append("用户：");
        b.append(userName);
        b.append("，在您的文章：");
        b.append(title);
        b.append("，下发表评论：");
        b.append(comment);
        return b.toString();
    }

    /**
     *
     * @param userName 用户名
     * @param comment 被回复的评论信息
     * @param commentReply 回复的评论信息
     * @return
     *  完整的系统提示信息
     */
    public static String getCommentReplyString(@NonNull String userName, @NonNull String comment,
                                               @NonNull String commentReply) {
        StringBuilder b = new StringBuilder();
        b.append("用户：");
        b.append(userName);
        b.append("，回复了您的评论：");
        b.append(comment);
        b.append("，回复内容为：");
        b.append(commentReply);
        return b.toString();
    }

    /**
     *  获取用户文章被收藏时的系统提示信息
     * @param userName 用户名
     * @param title 文章名
     * @return
     *  完整的系统提示信息
     */
    @NonNull
    public static String getCollectionString(@NonNull String userName, @NonNull String title) {
        StringBuilder b = new StringBuilder();
        b.append("用户：");
        b.append(userName);
        b.append("，收藏了您的文章：");
        b.append(title);
        return b.toString();
    }

    /**
     *  获取用户文章被收藏时的系统提示信息
     * @param userName 用户名
     * @param title 文章名
     * @return
     *  完整的系统提示信息
     */
    @NonNull
    public static String getThumbsString(@NonNull String userName, @NonNull String title) {
        StringBuilder b = new StringBuilder();
        b.append("用户：");
        b.append(userName);
        b.append("，点赞了您的文章：");
        b.append(title);
        return b.toString();
    }

}

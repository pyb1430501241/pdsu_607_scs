package com.pdsu.scs.handler;

import com.pdsu.scs.bean.Result;
import com.pdsu.scs.exception.web.DeleteInforException;
import com.pdsu.scs.exception.web.blob.NotFoundBlobIdException;
import com.pdsu.scs.exception.web.blob.RepetitionThumbsException;
import com.pdsu.scs.exception.web.blob.comment.NotFoundCommentIdException;
import com.pdsu.scs.exception.web.es.InsertException;
import com.pdsu.scs.exception.web.es.QueryException;
import com.pdsu.scs.exception.web.file.UidAndTitleRepetitionException;
import com.pdsu.scs.exception.web.user.*;
import com.pdsu.scs.exception.web.user.email.NotFoundEmailException;
import org.apache.commons.mail.EmailException;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author 半梦
 * @create 2020-08-29 15:19
 * 该接口提供一些系统所需的默认参数和异常处理方案
 * 异常的抛出逻辑有很多不合理之处, 懒得改了, 如果想优化
 * 查询不到时, 不要抛出异常, 返回 null即可
 * 只有在进行插入, 修改, 删除操作时, 抛出异常
 */
public interface AbstractHandler {

    /**
     * 默认博客图片储存地址
     */
    String DEFAULT_BLOB_IMG_FILEPATH = "/pdsu/web/blob/img/";

    /**
     * 默认上传文件储存地址
     */
    String DEFAULT_FILE_FILEPATH = "/pdsu/web/file/";

    /**
     * 默认头像储存地址
     */
    String DEFAULT_USER_IMG_FILEPATH = "/pdsu/web/img/";

    /**
     * 默认图片后缀名
     */
    String DEFAULT_IMG_SUFFIX = ".jpg";

    /**
     * 默认图片后缀名
     */
    String DEFAULT_IMG_SUFFIX_EXCEPT_POINT = "jpg";

    /**
     * 默认头像
     */
    String DEFAULT_USER_IMG_NAME = "422696839bb3222a73a48d7c97b1bba4.jpg";

    /**
     * 是否有下一页的参数名
     */
    String HAS_NEXT_PAGE = "hasNextPage";

    /**
     * 处理 NotFoundBlobIdException 异常
     * @param e 异常名
     * @return
     *  错误信息
     */
    @ExceptionHandler(NotFoundBlobIdException.class)
    @ResponseBody
    public Result processNotFoundBlobIdException(NotFoundBlobIdException e);

    /**
     * 处理 UnsupportedEncodingException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(UnsupportedEncodingException.class)
    @ResponseBody
    public Result processUnsupportedEncodingException(UnsupportedEncodingException e);

    /**
     * 处理 Exception 异常, 优先级最低
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result processException(Exception e);

    /**
     * 处理 UserNotLoginException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(UserNotLoginException.class)
    @ResponseBody
    public Result processUserNotLoginException(UserNotLoginException e);

    /**
     * 处理 UidAndWebIdRepetitionException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(UidAndWebIdRepetitionException.class)
    @ResponseBody
    public Result processUidAndWebIdRepetitionException(UidAndWebIdRepetitionException e);

    /**
     * 处理 DeleteInforException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(DeleteInforException.class)
    @ResponseBody
    public Result processDeleteInforException(DeleteInforException e);

    /**
     * 处理 NotFoundCommentIdException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(NotFoundCommentIdException.class)
    @ResponseBody
    public Result processNotFoundCommentIdException(NotFoundCommentIdException e);

    /**
     * 处理 NotFoundUidException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(NotFoundUidException.class)
    @ResponseBody
    public Result processNotFoundUidException(NotFoundUidException e);

    /**
     * 处理 RepetitionThumbsException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(RepetitionThumbsException.class)
    @ResponseBody
    public Result processRepetitionThumbsException(RepetitionThumbsException e);

    /**
     * 处理 IOException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(IOException.class)
    @ResponseBody
    public Result processIOException(IOException e);

    /**
     * 处理 AuthenticationException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public Result processAuthenticationException(AuthenticationException e);

    /**
     * 处理 EmailException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(EmailException.class)
    @ResponseBody
    public Result processEmailException(EmailException e);

    /**
     * 处理 UidRepetitionException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(UidRepetitionException.class)
    @ResponseBody
    public Result processUidRepetitionException(UidRepetitionException e);

    /**
     * 处理 NotFoundEmailException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(NotFoundEmailException.class)
    @ResponseBody
    public Result processNotFoundEmailException(NotFoundEmailException e);

    /**
     * 处理 UidAndLikeIdRepetitionException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(UidAndLikeIdRepetitionException.class)
    @ResponseBody
    public Result processUidAndLikeIdRepetitionException(UidAndLikeIdRepetitionException e);

    /**
     * 处理 NotFoundUidAndLikeIdException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(NotFoundUidAndLikeIdException.class)
    @ResponseBody
    public Result processNotFoundUidAndLikeIdException(NotFoundUidAndLikeIdException e);

    /**
     * 处理 QueryException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(QueryException.class)
    @ResponseBody
    public abstract Result processQueryException(QueryException e);

    /**
     * 处理 UidAndTitleRepetitionException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(UidAndTitleRepetitionException.class)
    @ResponseBody
    public Result processUidAndTitleRepetitionException(UidAndTitleRepetitionException e);

    /**
     * 处理 InsertException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(InsertException.class)
    @ResponseBody
    public abstract Result processInsertException(InsertException e);

    /**
     * 处理 MethodArgumentTypeMismatchException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public Result processMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e);

    /**
     * 处理 BindException 异常
     * @ResponseStatus(HttpStatus.BAD_REQUEST)
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Result processBindException(BindException e);

    /**
     * 处理 BindException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public Result processMissingServletRequestParameterException(MissingServletRequestParameterException e);
}

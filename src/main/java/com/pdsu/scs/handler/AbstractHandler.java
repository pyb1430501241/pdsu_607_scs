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
 */
public abstract class AbstractHandler {

    protected static final String DEFAULT_BLOB_IMG_FILEPATH = "/pdsu/web/blob/img/";

    protected static final String DEFAULT_FILE_FILEPATH = "/pdsu/web/file/";

    protected static final String DEFAULT_USER_IMG_FILEPATH = "/pdsu/web/img/";

    protected static final String DEFAULT_IMG_SUFFIX = ".jpg";

    protected static final String DEFAULT_IMG_SUFFIX_EXCEPT_POINT = "jpg";

    protected static final String DEFAULT_USER_IMG_NAME = "422696839bb3222a73a48d7c97b1bba4.jpg";

    /**
     * 处理 NotFoundBlobIdException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(NotFoundBlobIdException.class)
    @ResponseBody
    public abstract Result processNotFoundBlobIdException(NotFoundBlobIdException e);

    /**
     * 处理 UnsupportedEncodingException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(UnsupportedEncodingException.class)
    @ResponseBody
    public abstract Result processUnsupportedEncodingException(UnsupportedEncodingException e);

    /**
     * 处理 Exception 异常, 优先级最低
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public abstract Result processException(Exception e);

    /**
     * 处理 UserNotLoginException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(UserNotLoginException.class)
    @ResponseBody
    public abstract Result processUserNotLoginException(UserNotLoginException e);

    /**
     * 处理 UidAndWebIdRepetitionException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(UidAndWebIdRepetitionException.class)
    @ResponseBody
    public abstract Result processUidAndWebIdRepetitionException(UidAndWebIdRepetitionException e);

    /**
     * 处理 DeleteInforException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(DeleteInforException.class)
    @ResponseBody
    public abstract Result processDeleteInforException(DeleteInforException e);

    /**
     * 处理 NotFoundCommentIdException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(NotFoundCommentIdException.class)
    @ResponseBody
    public abstract Result processNotFoundCommentIdException(NotFoundCommentIdException e);

    /**
     * 处理 NotFoundUidException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(NotFoundUidException.class)
    @ResponseBody
    public abstract Result processNotFoundUidException(NotFoundUidException e);

    /**
     * 处理 RepetitionThumbsException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(RepetitionThumbsException.class)
    @ResponseBody
    public abstract Result processRepetitionThumbsException(RepetitionThumbsException e);

    /**
     * 处理 IOException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(IOException.class)
    @ResponseBody
    public abstract Result processIOException(IOException e);

    /**
     * 处理 AuthenticationException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public abstract Result processAuthenticationException(AuthenticationException e);

    /**
     * 处理 EmailException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(EmailException.class)
    @ResponseBody
    public abstract Result processEmailException(EmailException e);

    /**
     * 处理 UidRepetitionException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(UidRepetitionException.class)
    @ResponseBody
    public abstract Result processUidRepetitionException(UidRepetitionException e);

    /**
     * 处理 NotFoundEmailException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(NotFoundEmailException.class)
    @ResponseBody
    public abstract Result processNotFoundEmailException(NotFoundEmailException e);

    /**
     * 处理 UidAndLikeIdRepetitionException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(UidAndLikeIdRepetitionException.class)
    @ResponseBody
    public abstract Result processUidAndLikeIdRepetitionException(UidAndLikeIdRepetitionException e);

    /**
     * 处理 NotFoundUidAndLikeIdException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(NotFoundUidAndLikeIdException.class)
    @ResponseBody
    public abstract Result processNotFoundUidAndLikeIdException(NotFoundUidAndLikeIdException e);

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
    public abstract Result processUidAndTitleRepetitionException(UidAndTitleRepetitionException e);

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
    public abstract Result processMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e);

    /**
     * 处理 BindException 异常
     * @ResponseStatus(HttpStatus.BAD_REQUEST)
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public abstract Result processBindException(BindException e);

    /**
     * 处理 BindException 异常
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public abstract Result processMissingServletRequestParameterException(MissingServletRequestParameterException e);
}

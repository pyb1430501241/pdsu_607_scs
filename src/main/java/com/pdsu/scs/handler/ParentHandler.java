/*
很难优化吧？
 */
package com.pdsu.scs.handler;

import com.pdsu.scs.bean.HandlerValueEnum;
import com.pdsu.scs.bean.Result;
import com.pdsu.scs.bean.UserInformation;
import com.pdsu.scs.exception.web.DeleteInforException;
import com.pdsu.scs.exception.web.blob.NotFoundBlobIdException;
import com.pdsu.scs.exception.web.blob.RepetitionThumbsException;
import com.pdsu.scs.exception.web.blob.comment.NotFoundCommentIdException;
import com.pdsu.scs.exception.web.es.InsertException;
import com.pdsu.scs.exception.web.es.QueryException;
import com.pdsu.scs.exception.web.file.UidAndTitleRepetitionException;
import com.pdsu.scs.exception.web.user.*;
import com.pdsu.scs.exception.web.user.email.NotFoundEmailException;
import com.pdsu.scs.utils.SimpleUtils;
import org.apache.commons.mail.EmailException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Properties;

/**
 * @author 半梦
 * @create 2020-08-19 19:51
 */
public abstract class ParentHandler implements AbstractHandler {

    protected static final String EXCEPTION = "exception";

    protected static final String DEFAULT_ERROR_PROMPT = "未定义类型错误";

    protected static final String NOT_LOGIN = "未登录";

    protected static final String ALREADY_LOGIN = "已登录";

    protected static final String CODE_EXPIRED = "验证码已过期, 请重新获取";

    protected static final String CODE_ERROR = "验证码错误";

    protected static final String NETWORK_BUSY = "网络繁忙, 请稍候重试";

    protected static final String INSUFFICIENT_PERMISSION = "权限不足";

    private static final Logger log = LoggerFactory.getLogger("异常处理日志");

    /**
     * 博客页面图片储存地址
     */
    protected static String Blob_Img_FilePath = DEFAULT_BLOB_IMG_FILEPATH;

    /**
     * 用户上传资源储存地址
     */
    protected static String File_FilePath = DEFAULT_FILE_FILEPATH;

    /**
     * 用户头像储存地址
     */
    protected static String User_Img_FilePath = DEFAULT_USER_IMG_FILEPATH;

    /**
     * 文件带点后缀名
     * 例: .jpg
     */
    protected static String Img_Suffix = DEFAULT_IMG_SUFFIX;

    /**
     * 文件后缀名
     * 例: jpg
     */
    protected static String Img_Suffix_Except_Point = DEFAULT_IMG_SUFFIX_EXCEPT_POINT;

    /**
     * 用户默认头像名
     */
    protected static String Default_User_Img_Name = DEFAULT_USER_IMG_NAME;

    protected void loginOrNotLogin(UserInformation user) throws UserNotLoginException {
        if(Objects.isNull(user)) {
            throw new UserNotLoginException();
        }
    }

    private static void initProperties() {
        log.info("系统配置初始化...");
        Properties properties = new Properties();
        try {
            ClassLoader classLoader = ParentHandler.class.getClassLoader();
            InputStream in = classLoader.getResourceAsStream("properties/csc.properties");
            properties.load(in);
            Enumeration enumeration = properties.propertyNames();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                String value = properties.getProperty(key);
                switch (HandlerValueEnum.getByKey(key)) {
                    case IMG_SUFFIX :
                        Img_Suffix = value;
                        Img_Suffix_Except_Point = SimpleUtils.getSuffixNameExceptPoint(value);
                        break;
                    case FILE_FILEPATH :
                        File_FilePath = value;
                        break;
                    case USER_IMG_FILEPATH:
                        User_Img_FilePath = value;
                        break;
                    case BLOB_IMG_FILEPATH:
                        Blob_Img_FilePath = value;
                        break;
                    case USER_IMG_NAME:
                        Default_User_Img_Name = value;
                        break;
                    default:
                }
            }
        } catch (IOException e) {
            log.warn("初始化配置失败...");
        }
        log.info("系统初始化成功...");
    }

    private static void mkdirs(){
        log.info("创建系统所需文件...");
        File file = new File(User_Img_FilePath);
        if(!file.exists()) {
            file.mkdirs();
        }
        file = new File(Blob_Img_FilePath);
        if(!file.exists()) {
            file.mkdirs();
        }
        file = new File(File_FilePath);
        if(!file.exists()) {
            file.mkdirs();
        }
        log.info("文件创建成功...");
    }

    static {
        initProperties();
        mkdirs();
    }

    @Override
    public Result processNotFoundBlobIdException(NotFoundBlobIdException e) {
        log.info("文章相关操作时出现未知错误, 原因: " + e.getMessage());
        return Result.notFound().add(EXCEPTION, e.getMessage());
    }

    @Override
    public Result processUnsupportedEncodingException(UnsupportedEncodingException e) {
        log.info("编码转换失败, 原因: " + e.getMessage());
        return Result.fail().add(EXCEPTION, DEFAULT_ERROR_PROMPT);
    }

    @Override
    public Result processException(Exception e) {
        log.error("项目运行发生未知错误, 原因: " + e);
        return Result.fail().add(EXCEPTION, DEFAULT_ERROR_PROMPT);
    }

    @Override
    public Result processUserNotLoginException(UserNotLoginException e) {
        log.info("使用某些功能时出现未知错误, 原因: " + NOT_LOGIN);
        return Result.fail().add(EXCEPTION, NOT_LOGIN);
    }

    @Override
    public Result processUidAndWebIdRepetitionException(UidAndWebIdRepetitionException e) {
        log.info("点赞和收藏出现未知错误, 原因: " + e.getMessage());
        return Result.fail().add(EXCEPTION, e.getMessage());
    }

    @Override
    public Result processDeleteInforException(DeleteInforException e) {
        log.info("进行删除相关操作时出现未知错误, 原因: " + e.getMessage());
        return Result.fail().add(EXCEPTION, DEFAULT_ERROR_PROMPT);
    }

    @Override
    public Result processNotFoundCommentIdException(NotFoundCommentIdException e) {
        log.info("评论相关操作出现未知错误, 原因: " + e.getMessage());
        return Result.fail().add(EXCEPTION, e.getMessage());
    }

    @Override
    public Result processNotFoundUidException(NotFoundUidException e) {
        log.info("用户不存在");
        return Result.fail().add(EXCEPTION, e.getMessage());
    }

    @Override
    public Result processRepetitionThumbsException(RepetitionThumbsException e) {
        log.info("用户点赞操作时出现未知错误, 原因: " + e.getMessage());
        return Result.fail().add(EXCEPTION, e.getMessage());
    }

    @Override
    public Result processIOException(IOException e) {
        log.error("文件操作出现未知错误, 原因: " + e.getMessage());
        return Result.fail().add(EXCEPTION, DEFAULT_ERROR_PROMPT);
    }

    @Override
    public Result processAuthenticationException(AuthenticationException e) {
        if(e instanceof IncorrectCredentialsException) {
            log.info("用户登录时出现未知错误, 原因: 账号或密码错误");
            return Result.fail().add(EXCEPTION, "账号或密码错误");
        } else if (e instanceof UnknownAccountException) {
            log.info("用户登录时出现未知错误, 原因: 账号不存在");
            return Result.fail().add(EXCEPTION, "账号不存在");
        } else if (e instanceof UserAbnormalException) {
            log.info("用户登录时出现未知错误, 原因: " + e.getMessage());
            return Result.fail().add(EXCEPTION, e.getMessage());
        }
        log.info("用户登录时出现未知错误, 原因: " + e.getMessage());
        SecurityUtils.getSubject().logout();
        return Result.fail().add(EXCEPTION, DEFAULT_ERROR_PROMPT);
    }

    @Override
    public Result processEmailException(EmailException e) {
        log.info("发送邮箱验证码出现未知错误, 原因: " + e.getMessage());
        return Result.fail().add(EXCEPTION, "邮箱地址不正确");
    }

    @Override
    public Result processUidRepetitionException(UidRepetitionException e) {
        log.info("用户申请账号时出现未知错误, 原因: " + e.getMessage());
        return Result.fail().add(EXCEPTION, "该学号已被注册");
    }

    @Override
    public Result processNotFoundEmailException(NotFoundEmailException e) {
        log.info("发送邮箱验证码出现未知错误, 原因: " + e.getMessage());
        return Result.fail().add(EXCEPTION, "未找到与该邮箱绑定的账号");
    }

    @Override
    public Result processUidAndLikeIdRepetitionException(UidAndLikeIdRepetitionException e) {
        log.info("用户进行关注相关操作时出现未知错误, 原因: " + e.getMessage());
        return Result.fail().add(EXCEPTION, e.getMessage());
    }

    @Override
    public Result processNotFoundUidAndLikeIdException(NotFoundUidAndLikeIdException e) {
        log.info("用户进行关注相关操作时出现未知错误, 原因: " + e.getMessage());
        return Result.fail().add(EXCEPTION, e.getMessage());
    }

    @Override
    public Result processQueryException(QueryException e) {
        log.info("ES进行查询时出现未知错误, 原因: " + e.getMessage());
        return Result.fail().add(EXCEPTION, NETWORK_BUSY);
    }

    @Override
    public Result processUidAndTitleRepetitionException(UidAndTitleRepetitionException e) {
        log.info("用户上传资源时出现未知错误, 原因: " + e.getMessage());
        return Result.fail().add(EXCEPTION, "无法上传同名文件, 请修改名称");
    }

    @Override
    public Result processInsertException(InsertException e) {
        log.info("ES进行插入操作时出现未知错误, 原因: " + e.getMessage());
        return Result.success();
    }

    @Override
    public Result processBindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        String errorMessage = "";
        for (FieldError error : bindingResult.getFieldErrors()) {
            errorMessage += error.getDefaultMessage() + " ";

        }
        log.info("请求API时, 参数不符合规范, 原因: " + errorMessage);
        return Result.bedRequest().add(EXCEPTION, errorMessage);
    }

    @Override
    public Result processMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.info("请求API时发生未知错误, 原因: " + e.getMessage());
        return Result.bedRequest().add(EXCEPTION, "参数类型错误");
    }

    @Override
    public Result processMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.info("请求API时发生未知错误, 原因: " + e.getMessage());
        return Result.bedRequest().add(EXCEPTION, "无效的请求地址或参数错误");
    }
}

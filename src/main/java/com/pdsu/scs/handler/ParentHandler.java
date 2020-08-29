package com.pdsu.scs.handler;

import com.pdsu.scs.utils.SimpleUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author 半梦
 * @create 2020-08-19 19:51
 */
public abstract class ParentHandler extends  AbstractHandler{

    protected static final String EXCEPTION = "exception";

    protected static final String DEFAULT_ERROR_PROMPT = "未定义类型错误";

    protected static final String NOT_LOGIN = "未登录";

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

    private static void initProperties() {
        Properties properties = new Properties();
        try {
            ClassLoader classLoader = ParentHandler.class.getClassLoader();
            InputStream in = classLoader.getResourceAsStream("properties/csc.properties");
            properties.load(in);
            Enumeration enumeration = properties.propertyNames();
            while (enumeration.hasMoreElements()) {
                String str = (String) enumeration.nextElement();
                String value = properties.getProperty(str);
                switch (str) {
                    case "imgSuffix" :
                        Img_Suffix = value;
                        Img_Suffix_Except_Point = SimpleUtils.getSuffixNameExceptPoint(value);
                        break;
                    case "fileFilePath" :
                        File_FilePath = value;
                        break;
                    case "userImgFilePath" :
                        User_Img_FilePath = value;
                        break;
                    case "blobImgFilePath" :
                        Blob_Img_FilePath = value;
                        break;
                    default:
                }
            }
        } catch (IOException e) {
        }
    }

    static {
        initProperties();
        File file = new File(User_Img_FilePath);
        if(file.exists()) {
            file.mkdirs();
        }
        file = new File(Blob_Img_FilePath);
        if(file.exists()) {
            file.mkdirs();
        }
        file = new File(File_FilePath);
        if(file.exists()) {
            file.mkdirs();
        }
    }

}

package com.pdsu.scs.exception.web.user;

/**
 * @author 半梦
 * @create 2020-08-31 23:40
 */
public class UserNotLoginException extends UserExpection {

    public UserNotLoginException(String exceptiopn) {
        super(exceptiopn);
    }

    public UserNotLoginException(){
        this("");
    }
}

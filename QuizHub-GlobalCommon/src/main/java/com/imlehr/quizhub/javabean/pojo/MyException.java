package com.imlehr.quizhub.javabean.pojo;

import lombok.Getter;

/**
 * @author Lehr
 * @create: 2020-03-26
 */
public class MyException extends Exception {

    @Getter
    private String errorCode;

    public MyException(String message,String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public MyException(String message, Throwable cause,String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public MyException(Throwable cause,String errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public MyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,String errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }
}

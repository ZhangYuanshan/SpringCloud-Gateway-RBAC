package com.quizhub.common.javabean;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Lehr
 * @create: 2020-03-26
 */
@Slf4j
public class MyException extends Exception {


    /**
     * 这个是返回码
     */
    @Getter
    private String retCode;

    /**
     * 这个是你希望返回的响应报文里的状态码，不写的时候默认是500
     */
    @Getter
    private Integer statusCode = 500;

    public MyException(String message, String retCode, Integer statusCode) {
        super(message);
        this.retCode = retCode;
        this.statusCode = statusCode;
    }

    public MyException(String message, Throwable cause, String retCode, Integer statusCode) {
        super(message, cause);
        this.retCode = retCode;
        this.statusCode = statusCode;
    }

    public MyException(Throwable cause, String retCode, Integer statusCode) {
        super(cause);
        this.retCode = retCode;
        this.statusCode = statusCode;
    }

    public MyException(String message, String retCode) {
        super(message);
        this.retCode = retCode;
    }

    public MyException(String message, Throwable cause, String retCode) {
        super(message, cause);
        log.warn(message,cause);
        this.retCode = retCode;
    }

    public MyException(Throwable cause, String retCode) {
        super(cause);
        this.retCode = retCode;
    }

    public MyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String retCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        log.warn(message);
        this.retCode = retCode;
    }
}

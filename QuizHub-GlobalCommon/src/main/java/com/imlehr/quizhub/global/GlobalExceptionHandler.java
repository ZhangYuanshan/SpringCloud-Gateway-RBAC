package com.imlehr.quizhub.global;


import com.imlehr.quizhub.javabean.pojo.MyException;
import com.imlehr.quizhub.javabean.pojo.ResponseMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.AccountLockedException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 这是用来处理全局报错的
 *
 * @author lehr
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常处理
     */
    @ExceptionHandler
    public ResponseMsg handleException(HttpServletRequest request, HttpServletResponse response, final Exception e) throws Exception {

        log.warn("遇到异常:"+e.getClass()+":"+e.getMessage(),e);

        if(e instanceof MyException)
        {
            MyException e1 = (MyException) e;
            response.setStatus(e1.getStatusCode());
            return ResponseMsg.ofFail(e1);
        }
        else
        {
            response.setStatus(500);
            return ResponseMsg.ofFail(new MyException(e.getMessage(),"500"));
        }

    }

}


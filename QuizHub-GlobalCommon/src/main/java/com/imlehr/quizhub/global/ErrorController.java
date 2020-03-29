package com.imlehr.quizhub.global;

import com.imlehr.quizhub.javabean.pojo.ErrorMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 对于filter里抛出的异常，全局处理器是处理不了的
 * 所以需要用到这个了
 * 专门用来捕获filter的异常
 */
@Slf4j
@RestController
public class ErrorController extends BasicErrorController {

    public ErrorController() {
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }

    @Override
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));

        HttpStatus status = getStatus(request);

        log.warn("在业务代码之外发生异常！现在用自定义的JSON格式返回结果");

        Map<String,Object> map = new ErrorMap<String,Object>(body);

        return new ResponseEntity<>(map, status);
    }
}
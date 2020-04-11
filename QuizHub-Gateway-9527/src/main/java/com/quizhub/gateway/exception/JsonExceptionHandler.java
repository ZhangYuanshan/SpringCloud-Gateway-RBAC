package com.quizhub.gateway.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lehr
 */
@Slf4j
public class JsonExceptionHandler extends DefaultErrorWebExceptionHandler {

    public JsonExceptionHandler(ErrorAttributes errorAttributes,
                                ResourceProperties resourceProperties,
                                ErrorProperties errorProperties,
                                ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {

        //获取异常
        Throwable error = super.getError(request);
        //打印错误
        log.warn(error.getMessage());

        //准备一个基本的错误m码
        String code = "500";
        String errMsg = "网关处理异常...";
        if (error instanceof org.springframework.cloud.gateway.support.NotFoundException) {
            //一般情况下可能是后台服务没有启动起来
            code = "503";
            errMsg = "目标服务正在维护中...";
        }
        if(error.getMessage().contains("404"))
        {
            code = "404";
            errMsg = "未能发现服务...";
        }

        //如果是服务端的话他会自己处理的......

        Map<String, Object> errorAttributes = new HashMap<>(8);

        errorAttributes.put("success", false);
        errorAttributes.put("retCode", code);
        errorAttributes.put("errMsg", errMsg);

        return errorAttributes;
    }

    /**
     * 规定返回json而不是html
     * @param errorAttributes
     * @return
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * 准备错误码
     * @param errorAttributes
     * @return
     */
    @Override
    protected HttpStatus getHttpStatus(Map<String, Object> errorAttributes) {
        return HttpStatus.valueOf(500);
    }
}
package com.quizhub.globalcommon.config;


import com.alibaba.fastjson.JSON;
import com.quizhub.globalcommon.javabean.pojo.ErrorMap;
import com.quizhub.globalcommon.javabean.pojo.ResponseMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局返回值统一封装
 *
 * @author lehr
 */
@EnableWebMvc
@Configuration
@Slf4j
public class GlobalReturnConfig {

    @RestControllerAdvice
    static class ResultResponseAdvice implements ResponseBodyAdvice<Object> {
        @Override
        public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
            return true;
        }

        @Override
        public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {


            //这个是swagger的页面的特殊处理
            String s = serverHttpRequest.getURI().toString();
            if(s.contains(".js")||s.contains(".html")|| s.contains("swagger")|| s.contains("api-docs") )
            {
                log.warn("swagger处理");
                return body;
            }



            //来自ErrorController的异常处理
            if (body instanceof ResponseMsg) {
                return body;
            }

            //外围的错误，被包装在了ErrorMap里
            if (body instanceof ErrorMap) {
                return ((ErrorMap) body).getResponseMsg();
            }

            if (body == null) {
                return ResponseMsg.ofSuccess();
            }

            if (body instanceof String) {
                return JSON.toJSON(ResponseMsg.ofSuccess(body)).toString();
            }

            return ResponseMsg.ofSuccess(body);

        }
    }


}
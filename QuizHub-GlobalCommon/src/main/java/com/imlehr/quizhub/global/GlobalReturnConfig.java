package com.imlehr.quizhub.global;


import com.alibaba.fastjson.JSON;
import com.imlehr.quizhub.javabean.pojo.ResponseMsg;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 全局返回值统一封装
 *
 * @author lehr
 */
@EnableWebMvc
@Configuration
public class GlobalReturnConfig {

    @RestControllerAdvice
    static class ResultResponseAdvice implements ResponseBodyAdvice<Object> {
        @Override
        public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
            return true;
        }

        //FIXME 基本类型出错  &  如果遇到404 405这种错误的话不知道为什么又会执行到这里
        @Override
        public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

            System.out.println("开始包装请求：");
            if(body!=null)
            {
                System.out.println(body.getClass());
            }


            //这个是swagger的页面的特殊处理
            String s = serverHttpRequest.getURI().toString();
            System.out.println(s);
            if(s.contains(".html")|| s.contains("swagger")|| s.contains("api-docs") )
            {
                System.out.println("swagger处理");
                return body;
            }


            if (body == null) {
                return ResponseMsg.ofSuccess();
            }
            if (body instanceof ResponseMsg) {
                return body;
            }
            //不知道为什么String类要特殊处理
            if (body instanceof String) {
                return JSON.toJSON(ResponseMsg.ofSuccess(body)).toString();
            }
            //为了解决globalException那里的问题我先暂时采用了下面这种判断手法：
            if (body.getClass().equals(LinkedHashMap.class)) {
                return body;
            }
            //而下面这个又是为了解决ErrorController和Filter报错....
            if (body.getClass().equals(HashMap.class)) {
                return body;
            }

            return ResponseMsg.ofSuccess(body);

        }
    }


}
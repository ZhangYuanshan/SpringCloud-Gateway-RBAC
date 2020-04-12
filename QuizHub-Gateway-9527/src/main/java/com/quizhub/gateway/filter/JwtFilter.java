package com.quizhub.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.quizhub.gateway.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;

/**
 * @author Lehr
 * @create: 2020-03-24
 */
@Component
@Slf4j
public class JwtFilter implements GlobalFilter, Order {


    @Autowired
    private AuthService authService;


    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //从请求头里获取token
        ServerHttpRequest request = exchange.getRequest();
        String token = request.getHeaders().getFirst("token");
        //从请求youcuo头里获取路径
        String uri = request.getURI().getPath();

        String method = request.getMethod().toString();

        //校验jwt的合法性，如果没有jwt或者失效了，都返回null
        JSONObject jsonJwt = authService.parseJwt(token);

        //判断当前用户是否有权限访问资源
        if(authService.hasAuth(uri,method,jsonJwt))
        {
            log.info("Auth checked!");
            //FIXME ： 暂时有问题 NPE
            request.mutate().header("userId",jsonJwt.getString("username"));
            //完成
            return chain.filter(exchange);
        }

        //权利不足，返回提示，有可能是权限不够或者没有登录或者token到期造成的
        log.info("Unauthorized! Request is denied!");

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        JSONObject message = new JSONObject();
        message.put("success", false);
        if(jsonJwt==null)
        {
            message.put("errCode", "401");
            message.put("errMsg", "需要登录");
        }
        else
        {
            message.put("errCode", "403");
            message.put("errMsg", "权限不足");
        }
        message.put("data", null);
        byte[] bits = message.toJSONString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int value() {
        return 0;
    }
}

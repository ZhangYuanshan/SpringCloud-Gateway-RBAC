package com.imlehr.quizhub.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;

/**
 * @author Lehr
 * @create: 2020-03-24
 */
@Component
@Slf4j
public class GlobalLogFilter implements GlobalFilter, Order {


    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        log.info("---------Hey----------QuizHub------------");
        String name = exchange.getRequest().getQueryParams().getFirst("name");
        if(name==null)
        {
            log.info("-----------no----------------");
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int value() {
        //决定filter先后顺序的
        return 0;
    }
}

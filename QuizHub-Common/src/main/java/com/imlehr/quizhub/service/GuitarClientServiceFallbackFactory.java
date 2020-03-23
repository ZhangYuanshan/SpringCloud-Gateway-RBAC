package com.imlehr.quizhub.service;

import com.imlehr.quizhub.javabean.vo.Guitar;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lehr
 * @create: 2020-03-23
 *
 */
@Component
public class GuitarClientServiceFallbackFactory implements FallbackFactory<GuitarClientService> {
    @Override
    public GuitarClientService create(Throwable cause) {
        return new GuitarClientService() {
            @Override
            public Guitar buyGuitar(@RequestParam("money") Integer money) {
                return new Guitar("Telecaster","Fender");
            }
        };
    }
}

package com.quizhub.gitserver.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lehr
 * @create: 2020-03-23
 */
@Component
@Slf4j
public class AuthenticateClientFallback implements AuthenticateClient {


    @Override
    public String httpAuth(@RequestParam("username") String username,
                            @RequestParam("password") String password){
        log.warn("权鉴模块调用失败");
        return "auth-fail";
    }
}





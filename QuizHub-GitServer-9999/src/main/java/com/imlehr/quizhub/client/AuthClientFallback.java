package com.imlehr.quizhub.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lehr
 * @create: 2020-03-23
 */
@Component
@Slf4j
public class AuthClientFallback implements AuthClient {


    @Override
    public String httpAuth(@RequestParam("username") String username,
                            @RequestParam("password") String password){
        log.warn("权鉴模块调用失败");
        return "auth-fail";
    }

    @Override
    public Boolean repoTransCheck(@RequestParam("visitorId") String visitorId,
                                  @PathVariable("service") String service,
                                  @PathVariable("owner") String owner,
                                  @PathVariable("repo") String repo){
        log.warn("权鉴模块调用失败");
        return false;
    }
}





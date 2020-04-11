package com.quizhub.gitserver.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lehr
 * @create: 2020-03-27
 */
@FeignClient(value="QuizHub-UAA")
public interface AuthenticateClient {

    @PostMapping("/auth/http-basic")
    String httpAuth(@RequestParam("username") String username,
                           @RequestParam("password") String password);

}

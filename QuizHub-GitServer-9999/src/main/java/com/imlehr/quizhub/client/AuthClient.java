package com.imlehr.quizhub.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lehr
 * @create: 2020-03-27
 */
@FeignClient(value="QuizHub-UAA")
public interface AuthClient {

    @PostMapping("/auth/http-basic")
    String httpAuth(@RequestParam("username") String username,
                           @RequestParam("password") String password);

    @PostMapping("/repoAuth/{owner}/{repo}/{service}")
    Boolean repoTransCheck(@RequestParam("visitorId") String visitorId,
                           @PathVariable("service") String service,
                           @PathVariable("owner") String owner,
                           @PathVariable("repo") String repo);




}

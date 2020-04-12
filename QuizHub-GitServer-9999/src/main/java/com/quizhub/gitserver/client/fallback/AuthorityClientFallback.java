package com.quizhub.gitserver.client.fallback;

import com.quizhub.gitserver.client.AuthorityClient;
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
public class AuthorityClientFallback implements AuthorityClient {


    @Override
    public Boolean repoTransCheck(@RequestParam("visitorId") String visitorId,
                                  @RequestParam("service") String service,
                                  @PathVariable("owner") String owner,
                                  @PathVariable("repo") String repo){
        log.warn("权鉴模块调用失败");
        return false;
    }
}





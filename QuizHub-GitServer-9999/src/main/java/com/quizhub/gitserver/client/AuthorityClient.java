package com.quizhub.gitserver.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lehr
 * @create: 2020-03-27
 */
@FeignClient(value="QuizHub-RBAC")
public interface AuthorityClient {


    @PostMapping("/gitRepos/{owner}/{repo}/authorities")
    Boolean repoTransCheck(@RequestParam("visitorId") String visitorId,
                           @RequestParam("service") String service,
                           @PathVariable("owner") String owner,
                           @PathVariable("repo") String repo);

}

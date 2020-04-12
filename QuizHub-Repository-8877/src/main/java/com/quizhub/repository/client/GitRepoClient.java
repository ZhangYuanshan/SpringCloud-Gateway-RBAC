package com.quizhub.repository.client;

import com.quizhub.common.javabean.ResponseMsg;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lehr
 * @create: 2020-03-27
 */
@FeignClient(value="QuizHub-GitServer")
public interface GitRepoClient {

    @GetMapping("/gitRepos/{owner}/{repoName}")
    ResponseMsg getRepo(@PathVariable("owner")String owner,
                        @PathVariable("repoName") String repoName);

}

package com.quizhub.repository.client.fallback;

import com.quizhub.common.javabean.ResponseMsg;
import com.quizhub.repository.client.GitRepoClient;
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
public class GitRepoFallback implements GitRepoClient {


    @Override
    public ResponseMsg getRepo(@PathVariable("owner")String owner,
                                @PathVariable("repoName") String repoName){
        log.warn("git模块调用失败");
        return ResponseMsg.ofFail("1234","git模块调用失败");
    }
}





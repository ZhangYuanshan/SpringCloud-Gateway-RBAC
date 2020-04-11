package com.quizhub.rbac.controller;

import com.quizhub.rbac.service.RepoAuthorityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lehr
 * @create: 2020-04-10
 */
@RequestMapping("/repos")
@RestController
@Slf4j
@Api(tags = "仓库权限接口")
public class RepoAuthorityController {

    @Autowired
    private RepoAuthorityService authorityService;

    @ApiOperation(value = "仓库权限检查", notes = "微服务内部调用，判断当前用户是否有权访问当前仓库")
    @GetMapping("{owner}/{repoName}/authorities")
    public Boolean repoCheck(String service,
                             @PathVariable("owner")String owner, @PathVariable("repoName") String repoName,
                             String visitorId) {
        log.info("进入权限检查阶段"+owner+","+repoName+"：visitorId:"+visitorId+"\tservice:"+service);
        return authorityService.repoAuthCheck(owner,repoName, visitorId, service);
    }




}

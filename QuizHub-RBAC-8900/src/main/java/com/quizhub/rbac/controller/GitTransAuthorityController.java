package com.quizhub.rbac.controller;

import com.quizhub.rbac.service.GitTransAuthorityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lehr
 * @create: 2020-04-10
 */
@RequestMapping("/gitRepos")
@RestController
@Api(tags = "传输权限接口")
public class GitTransAuthorityController {


    @Autowired
    private GitTransAuthorityService authorityService;

    @ApiOperation(value = "仓库权限检查", notes = "微服务内部调用，判断当前用户是否有权访问当前仓库")
    @PostMapping("{owner}/{repo}/authorities")
    public Boolean repoTransCheck(String service, @PathVariable("owner") String owner, @PathVariable("repo") String repo, String visitorId) {
        return authorityService.repoTransCheck(owner, repo, visitorId, service);
    }




}

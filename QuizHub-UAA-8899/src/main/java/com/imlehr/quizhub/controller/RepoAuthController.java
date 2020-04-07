package com.imlehr.quizhub.controller;

import com.imlehr.quizhub.service.RepoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;





/**
 * @author Lehr
 * @create: 2020-03-27
 */
@RestController
@Api(tags = "仓库权限接口")
@RequestMapping("repoAuth")
public class RepoAuthController {


    @Autowired
    private RepoService repoService;

    @ApiOperation(value = "仓库权限检查",notes = "微服务内部调用，判断当前用户是否有权访问当前仓库")
    @PostMapping("{owner}/{repo}/{service}")
    public Boolean repoTransCheck(@PathVariable("service")String service, @PathVariable("owner") String owner, @PathVariable("repo") String repo, String visitorId)
    {
        return repoService.repoTransCheck(owner,repo,visitorId,service);
    }

}

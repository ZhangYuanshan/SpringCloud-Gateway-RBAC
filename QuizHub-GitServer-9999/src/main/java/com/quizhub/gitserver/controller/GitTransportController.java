package com.quizhub.gitserver.controller;

import com.quizhub.gitserver.service.GitTransportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Lehr
 * @create: 2020-03-30
 * 智协议的两个实现
 */
@RestController
@Slf4j
@Api(tags = "git传输接口")
@RequestMapping("git")
public class GitTransportController {

    @Autowired
    private GitTransportService transportService;

    @SneakyThrows
    @ApiOperation(value = "拉取目录",notes = "http smart protocol第一步，由git bash发起")
    @GetMapping(value = "/{owner}/{repo}/info/refs")
    public void gitCgi(@PathVariable("owner")String owner,@PathVariable("repo") String repo, String service, HttpServletRequest req, HttpServletResponse res) {
        transportService.getRequest(repo,owner,service,req,res);

    }


    @SneakyThrows
    @ApiOperation(value = "检索文件",notes = "http smart protocol第二步，由git bash发起")
    @PostMapping(value = "{owner}/{repo}/{service}")
    public void postCgi(@RequestBody byte[] info,@PathVariable("owner")String owner, @PathVariable("repo") String repo, @PathVariable("service") String service, HttpServletRequest req, HttpServletResponse res) {

        transportService.postRequest(info,repo,owner,service,req,res);

    }
}

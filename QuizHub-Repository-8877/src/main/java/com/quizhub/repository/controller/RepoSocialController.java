package com.quizhub.repository.controller;

import com.quizhub.common.javabean.Pager;
import com.quizhub.common.utils.HeaderUtils;
import com.quizhub.repository.javabean.vo.RepoIntroVO;
import com.quizhub.repository.javabean.vo.RepoVO;
import com.quizhub.repository.service.RepoDataService;
import com.quizhub.repository.service.RepoSocialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lehr
 * @create: 2020-04-10
 * 对仓库的基本操作的接口
 */
@RestController
@RequestMapping("repos")
@Api(tags = "仓库互动接口")
@Slf4j
public class RepoSocialController {


    @Autowired
    private RepoSocialService repoService;

    @ApiOperation(value = "仓库点赞", notes = "给这个仓库点一个赞")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "owner", value = "仓库拥有者的id"),
            @ApiImplicitParam(name = "repoName", value = "仓库名称")
    })
    @PostMapping("/{owner}/{repoName}/stars")
    public void starRepo(@PathVariable("owner") String owner, @PathVariable("repoName") String repoName, HttpServletRequest request) {
        String visitorId = HeaderUtils.getVisitorId(request);
        repoService.starRepo(owner, repoName, visitorId);
    }


    @ApiOperation(value = "收藏仓库", notes = "把这个仓库放到自己的收藏夹里")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "owner", value = "仓库拥有者的id"),
            @ApiImplicitParam(name = "repoName", value = "仓库名称")
    })
    @PostMapping("/{owner}/{repoName}/forks")
    public void forkRepo(@PathVariable("owner") String owner, @PathVariable("repoName") String repoName, HttpServletRequest request) {
        String visitorId = HeaderUtils.getVisitorId(request);
        repoService.forkRepo(owner, repoName, visitorId);
    }

    @ApiOperation(value = "关注仓库", notes = "实时获得仓库的最新动态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "owner", value = "仓库拥有者的id"),
            @ApiImplicitParam(name = "repoName", value = "仓库名称")
    })
    @PostMapping("/{owner}/{repoName}/watches")
    public void watchRepo(@PathVariable("owner") String owner, @PathVariable("repoName") String repoName, HttpServletRequest request) {
        String visitorId = HeaderUtils.getVisitorId(request);
        repoService.watchRepo(owner, repoName, visitorId);
    }

    @ApiOperation(value = "点赞情况", notes = "查看这个仓库的点赞情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "owner", value = "仓库拥有者的id"),
            @ApiImplicitParam(name = "repoName", value = "仓库名称")
    })
    @GetMapping("/{owner}/{repoName}/stars")
    public Integer getStars(@PathVariable("owner") String owner, @PathVariable("repoName") String repoName) {
        return repoService.getStars(owner, repoName);
    }


    @ApiOperation(value = "收藏情况", notes = "查看这个仓库的fork情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "owner", value = "仓库拥有者的id"),
            @ApiImplicitParam(name = "repoName", value = "仓库名称")
    })
    @GetMapping("/{owner}/{repoName}/forks")
    public Integer getForks(@PathVariable("owner") String owner, @PathVariable("repoName") String repoName) {
        return repoService.getForks(owner, repoName);
    }

    @ApiOperation(value = "关注情况", notes = "查看这个仓库的关注情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "owner", value = "仓库拥有者的id"),
            @ApiImplicitParam(name = "repoName", value = "仓库名称")
    })
    @GetMapping("/{owner}/{repoName}/watches")
    public Integer getWatches(@PathVariable("owner") String owner, @PathVariable("repoName") String repoName) {
        return repoService.getWatches(owner, repoName);
    }


    @ApiOperation(value = "撤回点赞", notes = "取消点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "owner", value = "仓库拥有者的id"),
            @ApiImplicitParam(name = "repoName", value = "仓库名称")
    })
    @DeleteMapping("/{owner}/{repoName}/stars")
    public void removeStar(@PathVariable("owner") String owner, @PathVariable("repoName") String repoName, HttpServletRequest request) {
        String visitorId = HeaderUtils.getVisitorId(request);
        repoService.removeStars(owner, repoName, visitorId);
    }


    @ApiOperation(value = "取消收藏", notes = "放弃收藏这个仓库")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "owner", value = "仓库拥有者的id"),
            @ApiImplicitParam(name = "repoName", value = "仓库名称")
    })
    @DeleteMapping("/{owner}/{repoName}/forks")
    public void removeFork(@PathVariable("owner") String owner, @PathVariable("repoName") String repoName, HttpServletRequest request) {
        String visitorId = HeaderUtils.getVisitorId(request);
        repoService.removeForks(owner, repoName, visitorId);
    }

    @ApiOperation(value = "取消关注", notes = "停止关注这个仓库")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "owner", value = "仓库拥有者的id"),
            @ApiImplicitParam(name = "repoName", value = "仓库名称")
    })
    @DeleteMapping("/{owner}/{repoName}/watches")
    public void removeWatches(@PathVariable("owner") String owner, @PathVariable("repoName") String repoName, HttpServletRequest request) {
        String visitorId = HeaderUtils.getVisitorId(request);
        repoService.removeWatches(owner, repoName, visitorId);
    }


}

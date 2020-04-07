package com.imlehr.quizhub.controller;

import com.imlehr.quizhub.service.GitInfoServiceImpl;
import com.imlehr.quizhub.utils.RequestUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lehr
 * @create: 2020-03-30
 * 本controller全部都不会对外暴露
 * 微服务内部调用
 */
@RestController
@RequestMapping("gitRepos")
@Api(tags = "git仓库ui操作接口")
public class GitInfoController {


    @Autowired
    GitInfoServiceImpl service;

    /**
     * 仓库更新触发的记录钩子
     */
    @ApiOperation(value = "更新记录",notes = "由服务器上的仓库每次被更新后钩子自动触发")
    @PostMapping("logs")
    public void pushLog()
    {
        System.out.println("----------------------adding------------------------");
    }


    /**
     * 获取指定仓库的日志
     * @param repoName
     * @return
     */
    @SneakyThrows
    @ApiOperation(value = "日志获取",notes = "获取这个仓库的全部日志")
    @GetMapping("{owner}/{repoName}/logs")
    public String getLog(@PathVariable("owner")String owner,@PathVariable("repoName") String repoName) {
        return  service.gitLog(owner,repoName);
    }

    /**
     * 获取某个仓库最新版本的所有文件的情况
     * @param repoName
     * @return
     */
    @SneakyThrows
    @ApiOperation(value = "查看仓库",notes = "获取这个仓库所有文件最新版本的信息")
    @GetMapping("{owner}/{repoName}")
    public String getRepo(@PathVariable("owner")String owner,@PathVariable("repoName") String repoName) {
        return  service.gitFiles(owner,repoName);
    }

    /**
     * 给一个仓库名字，创建一个新的仓库，强制生成一次readme
     * @param repoName
     * @return
     */
    @SneakyThrows
    @ApiOperation(value = "创建仓库",notes = "创建一个仓库，并获取访问链接")
    @PostMapping("{owner}/{repoName}")
    public String createRepo(@PathVariable("owner")String owner, @PathVariable("repoName") String repoName) {
        return service.createRepo(owner,repoName);
    }

    /**
     * 获取某种具体文件的信息，如果是目录就返回目录列表，如果是文件就返回文件内容的二进制信息
     * @param repoName
     * @param type
     * @param request
     * @return
     */
    @SneakyThrows
    @ApiOperation(value = "内容查询",notes = "查询具体某个资源，如果是文件就返回内容，如果是文件夹就返回目录")
    @GetMapping("{owner}/{repoName}/{type}/{brench}/**")
    public String getDetail(@PathVariable("owner")String owner,
                            @PathVariable("repoName") String repoName,
                            @PathVariable("type")String type, HttpServletRequest request) {

        String path = RequestUtils.extractPathFromPattern(request);

        Boolean isDir = null;
        if("tree".equals(type))
        {
            isDir = true;
        }
        else if("blob".equals(type))
        {
            isDir = false;
        }
        else{
            return "查找失败，类型不合法";
        }

        return service.getDetail(owner,repoName,path,isDir);

    }

    @SneakyThrows
    @ApiOperation(value = "web上传",notes = "通过web触发的文件上传操作")
    @PutMapping("{owner}/{repoName}")
    public String onlineUpload(@PathVariable("owner")String owner,
                               @PathVariable("repoName") String repoName,
                               String filename) {

        return service.onlineUpload(owner,repoName,filename);

    }

    @SneakyThrows
    @ApiOperation(value = "web删除",notes = "通过web触发的删除功能")
    @DeleteMapping("{owner}/{repoName}/{filename}")
    public String onlineDelete(@PathVariable("owner")String owner,
                               @PathVariable("repoName") String repoName,
                               @PathVariable("filename") String filename) {

        return service.onlineDelete(owner,repoName,filename);

    }


}

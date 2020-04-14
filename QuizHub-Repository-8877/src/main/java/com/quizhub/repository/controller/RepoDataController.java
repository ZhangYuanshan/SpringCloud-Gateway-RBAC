package com.quizhub.repository.controller;

import com.quizhub.common.javabean.Pager;
import com.quizhub.repository.javabean.vo.RepoIntroVO;
import com.quizhub.repository.javabean.vo.RepoVO;
import com.quizhub.repository.service.RepoDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "仓库基本接口")
@Slf4j
public class RepoDataController {


    @Autowired
    private RepoDataService repoService;

    @ApiOperation(value = "仓库总览",notes = "获取仓库的概览，通过listType指定不同的排序规则返回，登录后可以获取private部分")
    @ApiImplicitParams({
            @ApiImplicitParam(name="owner",value="仓库拥有者的id"),
            @ApiImplicitParam(name="listType",value="排序类型，比如：置顶，最活跃，时间顺序等等，具体以后商量"),
            @ApiImplicitParam(name="pageNo",value="页数"), @ApiImplicitParam(name="pageSize",value="容量")
    })
    @GetMapping("/{owner}")
    public Pager<RepoIntroVO> listRepos(@PathVariable("owner")String owner, String listType,
                                        Integer pageNo, Integer pageSize)
    {
        log.info("正在获取仓库列表");
        return repoService.listRepos(owner, listType, pageNo, pageSize);
    }


    @ApiOperation(value = "asyn仓库具体",notes = "获取仓库主页所需的各种信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="owner",value="仓库拥有者的id"),
            @ApiImplicitParam(name="repoName",value="仓库名称")
    })
    @GetMapping("asyn/{owner}/{repoName}")
    public RepoVO getRepo(@PathVariable("owner")String owner, @PathVariable("repoName")String repoName)
    {
        log.info("正在获取仓库详细内容");
        return repoService.getRepo(owner, repoName);
    }


    @ApiOperation(value = "sync仓库具体",notes = "获取仓库主页所需的各种信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="owner",value="仓库拥有者的id"),
            @ApiImplicitParam(name="repoName",value="仓库名称")
    })
    @GetMapping("/{owner}/{repoName}")
    public RepoVO getRepoSyn(@PathVariable("owner")String owner, @PathVariable("repoName")String repoName)
    {
        log.info("正在获取仓库详细内容");
        return repoService.getRepoSyn(owner, repoName);
    }

    @ApiOperation(value = "创建仓库",notes = "创建一个仓库")
    @ApiImplicitParams({
            @ApiImplicitParam(name="repoName",value="仓库名称"),
            @ApiImplicitParam(name="intro",value="仓库简介"),
            @ApiImplicitParam(name="isPublic",value="是否公开，true or false")
    })
    @PostMapping("")
    public void createRepo(String repoName, String intro, Boolean isPublic, HttpServletRequest request)
    {
        String owner = request.getHeader("userId");
        log.info("正在创建仓库");
        repoService.createRepo(owner,repoName,intro,isPublic);
    }


    @ApiOperation(value = "删除仓库",notes = "删除一个仓库")
    @ApiImplicitParams({
            @ApiImplicitParam(name="repoName",value="仓库名称"),
            @ApiImplicitParam(name="owner",value="仓库拥有者的id")
    })
    @DeleteMapping("/{owner}/{repoName}")
    public void removeRepo(@PathVariable("owner")String owner,
                           @PathVariable("repoName")String repoName)
    {
        //throw new MyException();
        log.info("正在销毁仓库");
        repoService.removeRepo(owner,repoName);
    }


    @ApiOperation(value = "在线修改",notes = "上传文件or删除文件，看service决定，filePath是删除的路径，file是上传的文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name="repoName",value="仓库名称"),
            @ApiImplicitParam(name="owner",value="仓库拥有者的id"),
            @ApiImplicitParam(name="service",value="是上传文件操作还是删除文件操作,还是编辑仓库简介,upload-file or delete-file,edit-intro"),
            @ApiImplicitParam(name="filePath",value="文件路径，是删除文件用的"),
            @ApiImplicitParam(name="file",value="MultipartFile文件，是上传才用的"),
            @ApiImplicitParam(name="intro",value="仓库简介，是修改简介才需要用的")
    })
    @PutMapping("/{owner}/{repoName}")
    public void onlineEdit(@PathVariable("owner")String owner, @PathVariable("repoName")String repoName,
                           @RequestParam(value = "file",required = false) MultipartFile file,
                           @RequestParam(required = false) String service,
                           @RequestParam(required = false) String filePath,
                           @RequestParam(required = false) String intro
                           )
    {
        //throw new MyException();
        log.info("正在使用web页面直接修改仓库内容");
        if("upload-file".equals(service))
        {
            repoService.onlineAdd(owner,repoName,file);
        }
        if("delete-file".equals(service)){
            repoService.onlineRemove(owner,repoName,filePath);
        }
        if("edit-intro".equals(service)){
            repoService.updateIntro(owner,repoName,intro);
        }


    }

}

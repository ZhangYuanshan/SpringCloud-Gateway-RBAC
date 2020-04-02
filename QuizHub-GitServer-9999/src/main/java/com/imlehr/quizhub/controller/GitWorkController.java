package com.imlehr.quizhub.controller;

import com.imlehr.quizhub.javabean.GitObject;
import com.imlehr.quizhub.service.GitService;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author Lehr
 * @create: 2020-03-30
 */
@RestController
@RequestMapping("lehr")
public class GitWorkController {


    @Autowired
    GitService service;

    @GetMapping("github/addlog")
    public void pushLog()
    {
        System.out.println("----------------------adding------------------------");
    }

    @SneakyThrows
    @GetMapping("github/create/{repoName}")
    public String createRepo(@PathVariable("repoName") String repoName) {
        return service.createRepo(repoName);
    }


    @SneakyThrows
    @GetMapping("github/log/{repoName}")
    public String gitLog(@PathVariable("repoName") String repoName) {
        return  service.gitLog(repoName);
    }


    @SneakyThrows
    @GetMapping("github/files/{repoName}")
    public String gitRepo(@PathVariable("repoName") String repoName) {
        return  service.gitFiles(repoName);
    }

    private static String extractPathFromPattern(
            final HttpServletRequest request)
    {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
    }


    @SneakyThrows
    @GetMapping("github/files/{repoName}/{type}/{brench}/**")
    public String getDetail(@PathVariable("repoName") String repoName, @PathVariable("type")String type, HttpServletRequest request) {


        String path = extractPathFromPattern(request);

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

        return service.getDetail(repoName,path,isDir);

    }


    @SneakyThrows
    @PostMapping("github/publicKey")
    public String addKey(String key)
    {
        return service.addKey(key);
    }

    @SneakyThrows
    @PostMapping("github/files/{repoName}/upload")
    public String onlineUpload(@PathVariable("repoName") String repoName,String filename) {

        return service.onlineUpload(repoName,filename);

    }

    @SneakyThrows
    @PostMapping("github/files/{repoName}/delete")
    public String onlineDelete(@PathVariable("repoName") String repoName,String path) {

        return service.onlineDelete(repoName,path);

    }


}

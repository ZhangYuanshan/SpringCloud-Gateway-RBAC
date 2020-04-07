package com.imlehr.quizhub.controller;

import com.imlehr.quizhub.javabean.po.ResponseMsg;
import com.imlehr.quizhub.javabean.po.UserDTO;
import com.imlehr.quizhub.service.AccountService;
import com.imlehr.quizhub.service.RepoService;
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
@RequestMapping("repoAuth")
public class RepoAuthController {


    @Autowired
    private RepoService repoService;

    @PostMapping("{owner}/{repo}/{service}")
    public Boolean repoTransCheck(@PathVariable("service")String service, @PathVariable("owner") String owner, @PathVariable("repo") String repo, String visitorId)
    {
        return repoService.repoTransCheck(owner,repo,visitorId,service);
    }

}

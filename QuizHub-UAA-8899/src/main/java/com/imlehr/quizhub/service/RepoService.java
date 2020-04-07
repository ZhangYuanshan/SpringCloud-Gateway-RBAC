package com.imlehr.quizhub.service;

import com.imlehr.quizhub.dao.RepoAuthMapper;
import com.imlehr.quizhub.dao.UserMapper;
import com.imlehr.quizhub.javabean.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author Lehr
 * @create: 2020-02-04
 */

@Service
public class RepoService {

    @Autowired
    private RepoAuthMapper repoAuthMapper;


    public Boolean repoTransCheck(String owner, String repo, String visitorId, String service)
    {
        RepoAuthPO repoAuthPO = repoAuthMapper.checkAuth(owner, repo, visitorId, service);
        return true;
    }
}

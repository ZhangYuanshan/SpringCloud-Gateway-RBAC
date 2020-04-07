package com.imlehr.quizhub.dao.impl;

import com.imlehr.quizhub.dao.RepoAuthMapper;
import com.imlehr.quizhub.javabean.po.RepoAuthPO;
import org.springframework.stereotype.Component;

/**
 * @author Lehr
 * @create: 2020-04-06
 */
@Component
public class RepoAuthMapperMockImpl implements RepoAuthMapper {


    @Override
    public RepoAuthPO checkAuth(String owner, String repo, String service, String visitorId) {
        return new RepoAuthPO().setRepoId("1").setRepoName("Lehr").setRepoOwner("Lehr130").setVisitorId("Lehr130").setService("all");
    }
}

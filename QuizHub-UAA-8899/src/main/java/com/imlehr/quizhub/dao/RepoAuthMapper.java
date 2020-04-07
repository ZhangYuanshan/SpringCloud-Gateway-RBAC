package com.imlehr.quizhub.dao;

import com.imlehr.quizhub.javabean.po.RepoAuthPO;
import com.imlehr.quizhub.javabean.po.UserBO;
import com.imlehr.quizhub.javabean.po.UserPO;
import org.springframework.stereotype.Component;

/**
 * @author Lehr
 * @create: 2020-03-30
 */
public interface RepoAuthMapper {

    RepoAuthPO checkAuth(String owner, String repo, String service, String visitorId);

}

package com.quizhub.rbac.service.serviceImpl;

import com.quizhub.rbac.service.RepoAuthorityService;
import org.springframework.stereotype.Service;

/**
 * @author Lehr
 * @create: 2020-04-10
 */
@Service
public class RepoAuthorityServiceImpl implements RepoAuthorityService {


    @Override
    public Boolean repoAuthCheck(String owner,String repoName, String visitorId, String service) {
        return true;
    }
}

package com.quizhub.rbac.service.serviceImpl;

import com.quizhub.rbac.service.GitTransAuthorityService;
import org.springframework.stereotype.Service;

/**
 * @author Lehr
 * @create: 2020-04-10
 */
@Service
public class GitTransAuthorityServiceImpl implements GitTransAuthorityService {

    @Override
    public Boolean repoTransCheck(String owner, String repo, String visitorId, String service) {
        return true;
    }


}

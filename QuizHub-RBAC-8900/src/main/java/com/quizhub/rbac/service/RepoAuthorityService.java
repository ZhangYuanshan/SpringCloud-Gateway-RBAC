package com.quizhub.rbac.service;

/**
 * @author Lehr
 * @create: 2020-04-10
 */
public interface RepoAuthorityService {

    Boolean repoAuthCheck(String owner, String repoName, String visitorId, String service);

}

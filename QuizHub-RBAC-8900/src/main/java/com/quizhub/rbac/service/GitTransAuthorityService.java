package com.quizhub.rbac.service;

/**
 * @author Lehr
 * @create: 2020-04-10
 */
public interface GitTransAuthorityService {

    Boolean repoTransCheck(String owner, String repo, String visitorId, String service);

}

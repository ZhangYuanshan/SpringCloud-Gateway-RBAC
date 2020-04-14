package com.quizhub.repository.dao;

import com.quizhub.repository.javabean.po.RepoPO;

/**
 * @author Lehr
 * @create: 2020-04-12
 */
public interface RepoDataMapper {

    RepoPO getRepo(String owner, String repoName);

    RepoPO listRepos(String owner,String listType);

    void addRepos();

    void removeRepos();

    void updateRepos();

}

package com.quizhub.repository.dao.mockImpl;

import com.quizhub.repository.dao.RepoMapper;
import com.quizhub.repository.javabean.po.RepoPO;
import org.springframework.stereotype.Repository;

/**
 * @author Lehr
 * @create: 2020-04-12
 */
@Repository
public class RepoMapperImpl implements RepoMapper {


    @Override
    public RepoPO getRepo(String owner, String repoName) {
        return new RepoPO().setRepoName("测试仓库")
                .setUsername("Lehr")
                .setTagName("微积分")
                .setForkNum(0).setStarNum(0).setWatchNum(0)
                .setIsPublic(false).setHttpUri("http://imlehr.com").setRepoIntro("一个很无聊的仓库");
    }

    @Override
    public RepoPO listRepos(String owner, String listType) {
        return null;
    }

    @Override
    public void addRepos() {

    }

    @Override
    public void removeRepos() {

    }

    @Override
    public void updateRepos() {

    }
}

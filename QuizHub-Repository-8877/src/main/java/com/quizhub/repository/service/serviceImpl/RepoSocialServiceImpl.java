package com.quizhub.repository.service.serviceImpl;


import com.google.common.collect.Lists;
import com.quizhub.common.javabean.Pager;
import com.quizhub.common.javabean.ResponseMsg;
import com.quizhub.common.javabean.RpcResponse;
import com.quizhub.mq.config.MQClient;
import com.quizhub.mq.javabean.enums.MqTags;
import com.quizhub.repository.client.GitRepoClient;
import com.quizhub.repository.dao.RepoDataMapper;
import com.quizhub.repository.javabean.po.RepoPO;
import com.quizhub.repository.javabean.vo.RepoIntroVO;
import com.quizhub.repository.javabean.vo.RepoVO;
import com.quizhub.repository.service.RepoDataService;
import com.quizhub.repository.service.RepoSocialService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Lehr
 * @create: 2020-04-11
 */
@Service
public class RepoSocialServiceImpl implements RepoSocialService {



    @Autowired
    private RepoDataMapper repoMapper;


    @Override
    public void starRepo(String owner, String repoName, String visitorId) {
        MQClient.sendMessage(MqTags.ACTIVITY,"有人点赞了你的仓库");

    }

    @Override
    public void forkRepo(String owner, String repoName, String visitorId) {
        MQClient.sendMessage(MqTags.ACTIVITY,"有人收藏了你的仓库");

    }

    @Override
    public void watchRepo(String owner, String repoName, String visitorId) {
        MQClient.sendMessage(MqTags.ACTIVITY,"有人关注了你的仓库");

    }


    @Override
    public Integer getStars(String owner, String repoName) {
        return null;
    }

    @Override
    public Integer getWatches(String owner, String repoName) {
        return null;
    }

    @Override
    public Integer getForks(String owner, String repoName) {
        return null;
    }










    @Override
    public void removeStars(String owner, String repoName, String visitorId) {
    }

    @Override
    public void removeForks(String owner, String repoName, String visitorId) {

    }

    @Override
    public void removeWatches(String owner, String repoName, String visitorId) {

    }
}

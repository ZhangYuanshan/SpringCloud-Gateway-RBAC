package com.quizhub.repository.service.serviceImpl;


import com.google.common.collect.Lists;
import com.quizhub.common.javabean.Pager;
import com.quizhub.common.javabean.ResponseMsg;
import com.quizhub.common.javabean.RpcResponse;
import com.quizhub.mq.config.MQClient;
import com.quizhub.mq.javabean.enums.MqTags;
import com.quizhub.repository.client.GitRepoClient;
import com.quizhub.repository.dao.RepoMapper;
import com.quizhub.repository.javabean.entity.FileInfo;
import com.quizhub.repository.javabean.po.RepoPO;
import com.quizhub.repository.javabean.vo.RepoIntroVO;
import com.quizhub.repository.javabean.vo.RepoVO;
import com.quizhub.repository.service.RepoService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Lehr
 * @create: 2020-04-11
 */
@Service
public class RepoServiceImpl implements RepoService {

    @Autowired
    private GitRepoClient gitRepoClient;

    @Autowired
    private RepoMapper repoMapper;


    @Override
    @SneakyThrows
    public RepoVO getRepo(String owner, String repoName) {


        Future<RpcResponse> future= getGitOverview(owner,repoName);

        RepoPO repoInfo = repoMapper.getRepo(owner, repoName);

        MQClient.sendMessage(MqTags.ACTIVITY,"访问了一次"+owner+"的"+repoName+"仓库");

        RpcResponse rpc = future.get();

        return new RepoVO(repoInfo).setReadmeContent(rpc.getEntity().get("readme").toString().getBytes()).setFileInfoList((List)rpc.getEntity().get("files"));
    }

    @Async
    public Future<RpcResponse> getGitOverview(String owner,String repoName)
    {
        ResponseMsg repo = gitRepoClient.getRepo(owner, repoName);

        RpcResponse rpc = new RpcResponse(repo);

        return new AsyncResult<>(rpc);
    }

    @Override
    public RepoVO getRepoSyn(String owner, String repoName) {

        ResponseMsg repo = gitRepoClient.getRepo(owner, repoName);

        RpcResponse rpc = new RpcResponse(repo);

        RepoPO repoInfo = repoMapper.getRepo(owner, repoName);

        MQClient.sendMessage(MqTags.ACTIVITY,"访问了一次"+owner+"的"+repoName+"仓库");

        return new RepoVO(repoInfo).setReadmeContent(rpc.getEntity().get("readme").toString().getBytes()).setFileInfoList((List)rpc.getEntity().get("files"));
    }

    @Override
    public Pager<RepoIntroVO> listRepos(String owner, String listType, Integer pageNo, Integer pageSize) {


        //获取分页结果
        return new Pager<RepoIntroVO>().setPageNo(1).setPageSize(10).setTotalPage(2).setTotalRow(22L)
                .setList(Lists.newArrayList(
                        new RepoIntroVO().setRepoName("测试仓库1").setTagName("微积分").setForkNum(0).setStarNum(0).setWatchNum(0).setIsPublic(false).setRepoIntro("一个很无聊的仓库"),
                        new RepoIntroVO().setRepoName("测试仓库2").setTagName("软工基").setForkNum(0).setStarNum(2).setWatchNum(0).setIsPublic(true).setRepoIntro("一个很好玩的仓库"),
                        new RepoIntroVO().setRepoName("测试仓库3").setTagName("体育课").setForkNum(0).setStarNum(0).setWatchNum(1).setIsPublic(true).setRepoIntro("一个很不好玩的仓库")
                ));
    }

    @Override
    public void onlineAdd(String owner, String repoName, MultipartFile file) {

    }

    @Override
    public void onlineRemove(String owner, String repoName, String filePath) {

    }

    @Override
    public void updateIntro(String owner, String repoName, String intro) {

    }

    @Override
    public void createRepo(String owner, String repoName, String intro, Boolean isPublic) {

    }

    @Override
    public void removeRepo(String owner, String repoName) {

    }
}

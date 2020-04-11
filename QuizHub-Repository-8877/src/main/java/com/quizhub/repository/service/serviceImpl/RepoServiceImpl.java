package com.quizhub.repository.service.serviceImpl;


import com.google.common.collect.Lists;
import com.quizhub.globalcommon.javabean.pojo.Pager;
import com.quizhub.repository.javabean.entity.FileInfo;
import com.quizhub.repository.javabean.vo.RepoIntroVO;
import com.quizhub.repository.javabean.vo.RepoVO;
import com.quizhub.repository.service.RepoService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Lehr
 * @create: 2020-04-11
 */
@Service
public class RepoServiceImpl implements RepoService {

    @Override
    public RepoVO getRepo(String owner, String repoName) {
        return new RepoVO()
                .setRepoName("测试仓库")
                .setUsername("Lehr")
                .setTagName("微积分").setReadmeContent("# Hi\n> readme.md".getBytes())
                .setForkNum(0).setStarNum(0).setWatchNum(0)
                .setIsPublic(false).setHttpUri("http://imlehr.com").setRepoIntro("一个很无聊的仓库")
                .setFileInfoList(
                        Lists.newArrayList(
                                new FileInfo().setFilename("半期考试答案").setCommitMessage("瞎写的").setCommitDate("昨天").setGitPath("/ad.d").setIsDir(false),
                                new FileInfo().setFilename("期末考试答案").setCommitMessage("瞎写的").setCommitDate("昨天").setGitPath("/ad.c").setIsDir(false),
                                new FileInfo().setFilename("月考试答案").setCommitMessage("瞎写的").setCommitDate("昨天").setGitPath("/ad.b").setIsDir(false),
                                new FileInfo().setFilename("平时作业大集合").setCommitMessage("瞎写的").setCommitDate("昨天").setGitPath("/ad.a").setIsDir(true)));
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

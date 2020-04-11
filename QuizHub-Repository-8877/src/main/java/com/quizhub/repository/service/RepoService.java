package com.quizhub.repository.service;

import com.quizhub.globalcommon.javabean.pojo.Pager;
import com.quizhub.repository.javabean.vo.RepoIntroVO;
import com.quizhub.repository.javabean.vo.RepoVO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Lehr
 * @create: 2020-04-11
 */
public interface RepoService {

    RepoVO getRepo(String owner,String repoName);

    Pager<RepoIntroVO> listRepos(String owner, String listType, Integer pageNo, Integer pageSize);

    void onlineAdd(String owner, String repoName, MultipartFile file);

    void onlineRemove(String owner,String repoName, String filePath);

    void updateIntro(String owner,String repoName, String intro);

    void createRepo(String owner,String repoName,String intro,Boolean isPublic);

    void removeRepo(String owner, String repoName);
}
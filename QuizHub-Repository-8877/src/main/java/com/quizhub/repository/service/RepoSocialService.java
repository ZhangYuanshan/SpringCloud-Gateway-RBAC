package com.quizhub.repository.service;

import com.quizhub.common.javabean.Pager;
import com.quizhub.repository.javabean.vo.RepoIntroVO;
import com.quizhub.repository.javabean.vo.RepoVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Lehr
 * @create: 2020-04-11
 */
public interface RepoSocialService {

    void starRepo(String owner,String repoName,String visitorId);

    void forkRepo(String owner,String repoName,String visitorId);

    void watchRepo(String owner,String repoName,String visitorId);

    Integer getStars(String owner,String repoName);

    Integer getWatches(String owner,String repoName);

    Integer getForks(String owner,String repoName);

    void removeStars(String owner,String repoName,String visitorId);

    void removeForks(String owner,String repoName,String visitorId);

    void removeWatches(String owner,String repoName,String visitorId);

}

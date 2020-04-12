package com.quizhub.gitserver.service;

import com.quizhub.common.javabean.MyException;
import com.quizhub.gitserver.javabean.dto.GitFileInfoDTO;
import com.quizhub.gitserver.javabean.dto.GitLogDTO;
import com.quizhub.gitserver.javabean.dto.GitOverviewDTO;
import com.quizhub.gitserver.javabean.dto.GitUrlDTO;

import java.util.List;

/**
 * @author Lehr
 * @create: 2020-04-01
 */
public interface GitInfoService {

    GitUrlDTO createRepo(String owner, String repoName)throws MyException;

    List<GitLogDTO> gitLog(String owner, String repoName) throws MyException;


    GitOverviewDTO repoOverview(String owner, String repoName) throws MyException;

    Object getDetail(String owner, String repoName, String path, Boolean isDir) throws MyException;

    void onlineUpload(String owner, String repoName, String filename) throws MyException;

    void onlineDelete(String owner, String repoName, String path) throws MyException;

}

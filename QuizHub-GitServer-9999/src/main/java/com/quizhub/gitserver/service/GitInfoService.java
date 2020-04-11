package com.quizhub.gitserver.service;

/**
 * @author Lehr
 * @create: 2020-04-01
 */
public interface GitInfoService {

    String createRepo(String owner, String repoName);


    String gitLog(String owner, String repoName);


    String gitFiles(String owner, String repoName);



    String getDetail(String owner, String repoName, String path, Boolean isDir);



    String onlineUpload(String owner, String repoName, String filename);



    String onlineDelete(String owner, String repoName, String path);

}

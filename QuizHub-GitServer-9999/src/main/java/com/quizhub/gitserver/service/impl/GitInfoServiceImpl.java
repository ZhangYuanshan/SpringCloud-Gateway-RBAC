package com.quizhub.gitserver.service.impl;

import com.quizhub.gitserver.javabean.GitFile;
import com.quizhub.gitserver.javabean.GitLog;
import com.quizhub.gitserver.javabean.GitObject;
import com.quizhub.gitserver.service.GitInfoService;
import com.quizhub.gitserver.utils.JGitUtils;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;

/**
 * @author Lehr
 * @create: 2020-04-01
 */
@Service
public class GitInfoServiceImpl implements GitInfoService {

    private static final String prefix = "null";
    private static final String SUFFIX = ".git";
    private static final String CONFIG = "/home/"+System.getProperty("user.name")+"/GitRepo/team-quizhub/";

    private Git initGit;

    //初始化环境配置
    @SneakyThrows
    public GitInfoServiceImpl() {

        //准备好本地仓库的文件引用
        File gitRepo = new File(CONFIG + SUFFIX);


        //如果不存在，就创建
        if (!gitRepo.exists()) {
            gitRepo.mkdirs();


            String s = "# Welcome\nIf you can see this, it means you successfully create this repo!\n>From Lehr";
            JGitUtils.createFile(CONFIG + "readme.md", s);


            //可能会报错但是我没有去处理
            JGitUtils.gitInit(false, gitRepo, "Team-Quizhub");

            //把readme放入仓库中
            initGit = Git.open(gitRepo);

            //完成初始化环境配置
            JGitUtils.addCommit(initGit, ":tada: Welcome to QuizHub!");
        }
        else {
            initGit = Git.open(gitRepo);
        }

    }

    //创建仓库
    @SneakyThrows
    @Override
    public String createRepo(String owner, String repoName) {

        //创建本地仓库
        String gitUrl = JGitUtils.getRepoPath(owner, repoName) + SUFFIX;
        File basic = new File(gitUrl);
        if (basic.exists()) {
            return "请换一个名字！";
        }

        //初始化仓库
        JGitUtils.gitInit(true, basic, owner);


        String s = "#!/bin/sh\n" +
                "\n" +
                "curl  http://localhost:9999/gitRepos/logs -X POST\n" +
                "\n" +
                "exec git update-server-info";

        //创建钩子文件
        File postHook = JGitUtils.createFile(gitUrl + "/hooks/post-update", s);

        //把钩子设置为可执行的  TODO: 现在把钩子放到前面是为了解决http同步的问题，主要就是需要一次回调...
        postHook.setExecutable(true);

        String sshUrl = System.getProperty("user.name")+"@localhost:GitRepo/" + owner + "/" + repoName + SUFFIX;
        String httpUrl = "http://localhost:9999/git/" + owner + "/" + repoName + SUFFIX;

        initGit.push().setRemote(sshUrl).call();

        //最后和bare仓库建立连接就好了
        return "仓库创建成功！\n" +
                "ssh链接是：" + sshUrl +
                "\nhttp链接是：" + httpUrl;
    }

    @SneakyThrows
    @Override
    public String gitLog(String owner, String repoName) {

        //创建本地仓库
        String gitUrl = JGitUtils.getRepoPath(owner, repoName+SUFFIX);

        File dir = new File(gitUrl);
        if (!dir.exists()) {
            return "仓库不存在";
        }

        StringBuilder sb = new StringBuilder("日志如下：\n");

        try (Git git = Git.open(dir)) {
            //获取所有版本号的迭代器
            for (RevCommit commit : git.log().call()) {
                sb.append(new GitLog(commit).toString());
            }
        }

        return sb.toString();

    }

    @SneakyThrows
    @Override
    public String gitFiles(String owner, String repoName) {

        String gitUrl = JGitUtils.getRepoPath(owner, repoName+SUFFIX);
        File dir = new File(gitUrl);
        if (!dir.exists()) {
            return "仓库不存在";
        }

        StringBuilder sb = new StringBuilder("文件目录：\n");

        try (Git git = Git.open(dir)) {
            LogCommand log = git.log();

            Repository repository = git.getRepository();

            TreeWalk tw = new TreeWalk(repository);

            //把 最新版本的树 的目录放入到TreeWalk里去准备遍历
            RevCommit current = log.call().iterator().next();
            tw.addTree(current.getTree());

            while (tw.next()) {
                sb.append(new GitObject(tw, repository, git).toString());
            }
        }

        //TODO：输出再排个序？

        return sb.toString();
    }


    @SneakyThrows
    @Override
    public String getDetail(String owner, String repoName, String path, Boolean isDir) {

        String gitUrl = JGitUtils.getRepoPath(owner, repoName+SUFFIX);

        File dir = new File(gitUrl);
        if (!dir.exists()) {
            return "仓库不存在";
        }

        try (Git git = Git.open(dir)) {
            LogCommand log = git.log();

            Repository repository = git.log().all().getRepository();

            TreeWalk tw = new TreeWalk(repository);

            //把最新版本的树目录放入到TreeWalk里去准备遍历
            tw.addTree(log.call().iterator().next().getTree());


            while (tw.next()) {

                String pathString = tw.getPathString();
                //第一层精确匹配
                if (path.startsWith(pathString)) {
                    //如果完全一样
                    if (path.equals(pathString)) {
                        if (!isDir.equals(tw.isSubtree())) {
                            return "文件搜索类型不对";
                        }
                        if (isDir) {
                            //执行文件目录遍历的操作
                            return getDir(tw, path, repository, git);
                        } else {
                            //执行文件内容抽取操作
                            return getFile(tw, repository);
                        }
                    }
                    //如果只是子目录，进入，递归
                    tw.enterSubtree();
                }
            }
        }

        return "寻找失败，没找到";


    }

    @SneakyThrows
    private String getDir(TreeWalk tw, String path, Repository repository, Git log) {
        StringBuilder sb = new StringBuilder();
        tw.enterSubtree();
        while (tw.next()) {
            if ((tw.getPathString() + "/").startsWith(path)) {
                sb.append(new GitObject(tw, repository, log).toString());
            } else {
                break;
            }

        }
        return sb.toString();
    }

    private String getFile(TreeWalk tw, Repository repository) {
        return new GitFile(tw, repository).toString();
    }

    @SneakyThrows
    @Override
    public String onlineUpload(String owner, String repoName, String filename) {

        String gitUrl = JGitUtils.getRepoPath(owner, repoName+SUFFIX);

        File dir = new File(gitUrl);
        if (!dir.exists()) {
            return "仓库不存在";
        }

        String temp = UUID.randomUUID().toString();

        String remoteLocal = System.getProperty("user.name")+"@localhost:GitRepo/"+owner+"/" + repoName +SUFFIX;

        File directory = JGitUtils.mkdir(owner+File.separator+temp);

        Git tempGit = Git.cloneRepository()
                .setURI(remoteLocal)
                .setDirectory(directory)
                .setCloneAllBranches(false)
                .call();


        String s = "# Uploading\nThis is web uploading temp file\n>From Lehr";
        JGitUtils.createFile("/home/"+System.getProperty("user.name")+"/GitRepo/"+owner+File.separator+temp+File.separator+filename,s);

        JGitUtils.addCommit(tempGit,"web uploading test");

        tempGit.push().setRemote(remoteLocal).call();

        //由于目录不是空目录，所以需要递归删除
        deleteDir(directory);

        return "上传完成，请检查";

    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    @SneakyThrows
    @Override
    public String onlineDelete(String owner, String repoName, String path) {


        String gitUrl = JGitUtils.getRepoPath(owner, repoName+SUFFIX);

        File dir = new File(gitUrl);
        if (!dir.exists()) {
            return "仓库不存在";
        }

        String temp = UUID.randomUUID().toString();

        String remoteLocal = System.getProperty("user.name")+"@localhost:GitRepo/"+owner+"/" + repoName +SUFFIX;

        File directory = JGitUtils.mkdir(owner+File.separator+temp);

        Git tempGit = Git.cloneRepository()
                .setURI(remoteLocal)
                .setDirectory(directory)
                .setCloneAllBranches(false)
                .call();

        File deletarget = JGitUtils.getFile(owner+File.separator+ temp + "/" + path);

        if (!deletarget.exists()) {
            return "文件不存在";
        }
        if (deletarget.isFile()) {
            deletarget.delete();
        } else {
            deleteDir(deletarget);
        }

        tempGit.add().setUpdate(true).addFilepattern(".").call();


        JGitUtils.addCommit(tempGit,"delete test!");

        tempGit.push().setRemote(remoteLocal).call();

        //由于目录不是空目录，所以需要递归删除
        deleteDir(directory);

        return "删除成功";

    }

}

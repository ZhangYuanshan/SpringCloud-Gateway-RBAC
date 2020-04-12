package com.quizhub.gitserver.service.impl;

import com.quizhub.common.javabean.MyException;
import com.quizhub.gitserver.javabean.dto.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Lehr
 * @create: 2020-04-01
 */
@Service
public class GitInfoServiceImpl implements GitInfoService {

    private static final String SUFFIX = ".git";
    private static final String CONFIG = "/home/" + System.getProperty("user.name") + "/GitRepo/team-quizhub/";

    private Git initGit;

    /**
     * 初始化环境配置
     */
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
        } else {
            initGit = Git.open(gitRepo);
        }

    }

    /**
     * 创建仓库
     *
     * @param owner
     * @param repoName
     * @return
     */
    @Override
    public GitUrlDTO createRepo(String owner, String repoName) throws MyException {

        //创建本地仓库
        String gitUrl = JGitUtils.getRepoPath(owner, repoName) + SUFFIX;
        File basic = new File(gitUrl);
        if (basic.exists()) {
            throw new MyException("仓库名称已被占用", "1008");
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

        String sshUrl = System.getProperty("user.name") + "@localhost:GitRepo/" + owner + "/" + repoName + SUFFIX;
        String httpUrl = "http://localhost:9999/git/" + owner + "/" + repoName + SUFFIX;

        try {
            initGit.push().setRemote(sshUrl).call();
        } catch (Exception e) {
            throw new MyException("初始化错误", "1004");
        }

        //最后和bare仓库建立连接就好了
        return new GitUrlDTO().setHttpUrl(httpUrl).setSshUrl(sshUrl);
    }

    @Override
    public List<GitLogDTO> gitLog(String owner, String repoName) throws MyException {

        //创建本地仓库
        String gitUrl = JGitUtils.getRepoPath(owner, repoName + SUFFIX);

        File dir = new File(gitUrl);
        if (!dir.exists()) {
            throw new MyException("仓库不存在", "1009");
        }

        List<GitLogDTO> logs = new ArrayList<>();

        try (Git git = Git.open(dir)) {
            //获取所有版本号的迭代器
            for (RevCommit commit : git.log().call()) {
                logs.add(new GitLogDTO(commit));
            }
        } catch (Exception e) {
            throw new MyException("仓库日志读取失败", "1019");
        }

        return logs;

    }


    @Override
    public GitOverviewDTO repoOverview(String owner, String repoName) throws MyException {

        String gitUrl = JGitUtils.getRepoPath(owner, repoName + SUFFIX);

        File dir = new File(gitUrl);
        if (!dir.exists()) {
            throw new MyException("仓库不存在", "1009");
        }

        List<GitFileInfoDTO> files = new ArrayList<>();

        try (Git git = Git.open(dir)) {
            LogCommand log = git.log();

            Repository repository = git.getRepository();

            TreeWalk tw = new TreeWalk(repository);

            //把 最新版本的树 的目录放入到TreeWalk里去准备遍历
            RevCommit current = log.call().iterator().next();
            tw.addTree(current.getTree());

            while (tw.next()) {
                files.add(new GitFileInfoDTO(tw, repository, git));
            }
        }catch (Exception e)
        {
            throw new MyException("查找出错！","12345");
        }

        //TODO：输出再排个序？
        byte[] readme = null;
        try{
            readme = ((GitFileContentDTO)getDetail(owner, repoName, "readme.md", false)).getContent();
        }catch (MyException e)
        {
            System.out.println("暂时没找到readme");
        }

        return new GitOverviewDTO().setFiles(files).setReadme(readme);


    }


    @Override
    public Object getDetail(String owner, String repoName, String path, Boolean isDir) throws MyException {

        String gitUrl = JGitUtils.getRepoPath(owner, repoName + SUFFIX);

        File dir = new File(gitUrl);
        if (!dir.exists()) {
            throw new MyException("仓库不存在", "1009");
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
        } catch (Exception e) {
            throw new MyException("文件遍历失败", "1109");
        }

        throw new MyException("没找到文件", "2109");


    }

    @SneakyThrows
    private List<GitFileInfoDTO> getDir(TreeWalk tw, String path, Repository repository, Git log) {
        List<GitFileInfoDTO> files = new ArrayList<>();

        tw.enterSubtree();
        while (tw.next()) {
            if ((tw.getPathString() + "/").startsWith(path)) {
                files.add(new GitFileInfoDTO(tw, repository, log));
            } else {
                break;
            }

        }

        return files;
    }

    private GitFileContentDTO getFile(TreeWalk tw, Repository repository) {
        return new GitFileContentDTO(tw, repository);
    }

    @Override
    public void onlineUpload(String owner, String repoName, String filename) throws MyException {

        String gitUrl = JGitUtils.getRepoPath(owner, repoName + SUFFIX);

        File dir = new File(gitUrl);
        if (!dir.exists()) {
            throw new MyException("仓库不存在", "1009");
        }

        try {

            String temp = UUID.randomUUID().toString();

            String remoteLocal = System.getProperty("user.name") + "@localhost:GitRepo/" + owner + "/" + repoName + SUFFIX;

            File directory = JGitUtils.mkdir(owner + File.separator + temp);

            Git tempGit = Git.cloneRepository()
                    .setURI(remoteLocal)
                    .setDirectory(directory)
                    .setCloneAllBranches(false)
                    .call();


            String s = "# Uploading\nThis is web uploading temp file\n>From Lehr";
            JGitUtils.createFile("/home/" + System.getProperty("user.name") + "/GitRepo/" + owner + File.separator + temp + File.separator + filename, s);

            JGitUtils.addCommit(tempGit, "web uploading test");

            tempGit.push().setRemote(remoteLocal).call();

            //由于目录不是空目录，所以需要递归删除
            deleteDir(directory);

        } catch (Exception e) {
            throw new MyException("添加失败", "1209");
        }

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

    @Override
    public void onlineDelete(String owner, String repoName, String path) throws MyException {


        String gitUrl = JGitUtils.getRepoPath(owner, repoName + SUFFIX);

        File dir = new File(gitUrl);
        if (!dir.exists()) {
            throw new MyException("仓库不存在", "1009");
        }

        try {


            String temp = UUID.randomUUID().toString();

            String remoteLocal = System.getProperty("user.name") + "@localhost:GitRepo/" + owner + "/" + repoName + SUFFIX;

            File directory = JGitUtils.mkdir(owner + File.separator + temp);

            Git tempGit = Git.cloneRepository()
                    .setURI(remoteLocal)
                    .setDirectory(directory)
                    .setCloneAllBranches(false)
                    .call();

            File deletarget = JGitUtils.getFile(owner + File.separator + temp + "/" + path);

            if (!deletarget.exists()) {
                throw new MyException("目标不存在", "1009");
            }
            if (deletarget.isFile()) {
                deletarget.delete();
            } else {
                deleteDir(deletarget);
            }

            tempGit.add().setUpdate(true).addFilepattern(".").call();


            JGitUtils.addCommit(tempGit, "delete test!");

            tempGit.push().setRemote(remoteLocal).call();

            //由于目录不是空目录，所以需要递归删除
            deleteDir(directory);
        } catch (MyException e) {
            throw e;
        } catch (Exception e) {
            throw new MyException("删除失败", "1309");
        }


    }

}

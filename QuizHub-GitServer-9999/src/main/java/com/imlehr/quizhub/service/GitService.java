package com.imlehr.quizhub.service;

import com.imlehr.quizhub.javabean.GitFile;
import com.imlehr.quizhub.javabean.GitLog;
import com.imlehr.quizhub.javabean.GitObject;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Lehr
 * @create: 2020-04-01
 */
@Service
public class GitService {

    private final static String suffix = ".git";
    //默认是放在这个文件夹里，另外的话就是，我把一个初始md也放在这里了，首次创建仓库的时候就会调用这个来加载
    private final static String prefix = "/home/lehr/GitRepo/Lehr130/";


    private final static String configUrl = "/home/lehr/GitRepo/config";
    private final static File readMe = new File(configUrl + "/readme.md");

    private Git helloGit = null;


    @SneakyThrows
    public GitService() {
        //创建本地仓库
        String gitUrl = configUrl + File.separator + suffix;
        File basic = new File(gitUrl);
        if (!basic.exists()) {
            basic.mkdirs();
        }


        if (!readMe.exists()) {
            readMe.createNewFile();
            FileOutputStream outStream = new FileOutputStream(readMe);
            String s = "# Welcome\nIf you can see this, it means you successfully create this repo!\n>From Lehr";
            outStream.write(s.getBytes());
            outStream.close();
        }


        //创建仓库，其中配置文件就是这里的，叫.git文件夹，正式内容在..上一级目录
        Git.init().setGitDir(basic)
                .setDirectory(basic.getParentFile()).call();

        //配置本地仓库
        try (
                Repository build = new RepositoryBuilder()
                        .setGitDir(basic)
                        .setMustExist(true)
                        .build()
        ) {

            build.getConfig().
                    setString(ConfigConstants.CONFIG_USER_SECTION, null,
                            ConfigConstants.CONFIG_KEY_NAME, "Lehr-QuizHub-Config");
            build.getConfig().save();

        }
        helloGit = Git.open(basic);
        helloGit.add().addFilepattern(".").call();

        CommitCommand commitCommand = helloGit.commit().setMessage("HeyLehr!").setAllowEmpty(true);
        commitCommand.call();

    }

    @SneakyThrows
    public String createRepo(String repoName) {

        //创建本地仓库
        String gitUrl = prefix + repoName + suffix;
        File basic = new File(gitUrl);
        if (basic.exists()) {
            return "请换一个名字！";
        }

        //创建仓库，其中配置文件就是这里的，叫.git文件夹，正式内容在..上一级目录
        Git.init().setGitDir(basic)
                .setDirectory(basic.getParentFile()).call();

        //配置本地仓库
        try (
                Repository build = new RepositoryBuilder()
                        .setGitDir(basic)
                        .setMustExist(true)
                        .build()
        ) {


            build.getConfig().
                    setString(ConfigConstants.CONFIG_USER_SECTION, null,
                            ConfigConstants.CONFIG_KEY_NAME, "Lehr-QuizHub-Team");

            build.getConfig().save();

        } catch (IOException e) {
            return "创建出问题了！";
        }


        String remoteLocal = "lehr@localhost:GitRepo/Lehr130/" + repoName + ".git";

        helloGit.push().setRemote(remoteLocal).call();

        //最后和bare仓库建立连接就好了
        return "仓库创建成功！ssh链接是：" + remoteLocal;
    }

    @SneakyThrows
    public String gitLog(String repoName) {

        //创建本地仓库
        String gitUrl = prefix + repoName + suffix;
        File dir = new File(gitUrl);
        if (!dir.exists()) {
            return "仓库不存在";
        }
        Git git = Git.open(dir);

        StringBuilder sb = new StringBuilder("日志如下：\n");

        //获取所有版本号的迭代器
        for (RevCommit commit : git.log().call()) {
            sb.append(new GitLog(commit).toString());
        }

        return sb.toString();

    }

    @SneakyThrows
    public String gitFiles(String repoName) {

        String gitUrl = prefix + repoName + suffix;
        File dir = new File(gitUrl);
        if (!dir.exists()) {
            return "仓库不存在";
        }

        Git git = Git.open(dir);

        StringBuilder sb = new StringBuilder("文件目录：\n");

        LogCommand log = git.log();

        Repository repository = git.log().all().getRepository();

        TreeWalk tw = new TreeWalk(repository);

        //把最新版本的树目录放入到TreeWalk里去准备遍历
        tw.addTree(log.call().iterator().next().getTree());

        while (tw.next()) {

            sb.append(new GitObject(tw, repository).toString());
        }

        return sb.toString();
    }


    @SneakyThrows
    public String getDetail(String repoName, String path, Boolean isDir) {

        String gitUrl = prefix + repoName + suffix;

        File dir = new File(gitUrl);
        if (!dir.exists()) {
            return "仓库不存在";
        }


        Git git = Git.open(dir);

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
                    if (isDir) {
                        //执行文件目录遍历的操作
                        return getDir(tw, path, repository);
                    } else {
                        //执行文件内容抽取操作
                        return getFile(tw, repository);
                    }
                }
                //如果只是子目录，进入，递归
                tw.enterSubtree();
            }
        }

        return "寻找失败，没找到";


    }

    @SneakyThrows
    private String getDir(TreeWalk tw, String path, Repository repository) {
        StringBuilder sb = new StringBuilder();
        tw.enterSubtree();
        while (tw.next()) {
            if ((tw.getPathString() + "/").startsWith(path)) {
                sb.append(new GitObject(tw, repository).toString());
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
    public String onlineUpload(String repoName, String filename) {
        //先去目标地点创建一个文件

        //把这个文件上传了
        //创建本地仓库
        String temp = UUID.randomUUID().toString();


        String remoteLocal = "lehr@localhost:GitRepo/Lehr130/" + repoName + ".git";

        Git tempGit = Git.cloneRepository()
                .setURI(remoteLocal)
                .setDirectory(new File(prefix+temp))
                .setCloneAllBranches(false)
                .call();


        File tempUploadFile = new File(prefix+temp + "/"+filename);
        tempUploadFile.createNewFile();
        FileOutputStream outStream = new FileOutputStream(tempUploadFile);
        String s = "# Uploading\nThis is web uploading temp file\n>From Lehr";
        outStream.write(s.getBytes());
        outStream.close();



        tempGit.add().addFilepattern(".").call();

        CommitCommand commitCommand = tempGit.commit().setMessage("WebUploading Test!").setAllowEmpty(true);
        commitCommand.call();


        tempGit.push().setRemote(remoteLocal).call();


        return "上传完成，请检查";

    }

}

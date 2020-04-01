package com.imlehr.quizhub.controller;

import com.imlehr.quizhub.GitServerApplication;
import com.imlehr.quizhub.git.MyRevFilter;
import com.imlehr.quizhub.javabean.GitObject;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.StopWalkException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Lehr
 * @create: 2020-03-30
 */
@RestController
@RequestMapping("lehr")
public class GitWorkController {

    private final static String suffix = ".git";
    //默认是放在这个文件夹里，另外的话就是，我把一个初始md也放在这里了，首次创建仓库的时候就会调用这个来加载
    private final static String prefix = "/home/lehr/GitRepo/Lehr130/";
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    private final static String configUrl = "/home/lehr/GitRepo/config";
    private final static File readMe = new File(configUrl + "/readme.md");

    private Git helloGit = null;

    //初始化的时候来创建readMe文件？
    @SneakyThrows
    public GitWorkController() {

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
    @GetMapping("github/create/{repoName}")
    public String git(@PathVariable("repoName") String repoName) {

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


    /**
     * 裸仓库是不能查看工作区等等文件变更的，只能去查看版本区
     *
     * @param repoName
     * @return
     */
//    @SneakyThrows
//    @GetMapping("github/status/{repoName}")
//    public String gitStatus(@PathVariable("repoName") String repoName) {
//
//        //创建本地仓库
//        String gitUrl = prefix + repoName + suffix;
//        File dir = new File(gitUrl);
//        if (!dir.exists()) {
//            return "仓库不存在";
//        }
//        Git git = Git.open(dir);
//
//        //返回的值都是相对工作区的路径，而不是绝对路径
//        Status status = git.status().call();
//
//        StringBuilder sb = new StringBuilder("修改记录：\n");
//        //git add命令后会看到变化
//        status.getAdded().forEach(it -> sb.append("*使用add添加了文件的：" + it + "\n"));
//        ///git rm命令会看到变化，从暂存区删除的文件列表
//        status.getRemoved().forEach(it -> sb.append("*从暂存区删除的文件：" + it + "\n"));
//        //修改的文件列表
//        status.getModified().forEach(it -> sb.append("*修改的文件：" + it + "\n"));
//        //冲突的文件列表
//        status.getConflicting().forEach(it -> sb.append("*冲突的文件：" + it + "\n"));
//        //工作区新增的文件列表
//        status.getUntracked().forEach(it -> sb.append("*工作区新增的文件：" + it + "\n"));
//        //工作区删除的文件列表
//        status.getMissing().forEach(it -> sb.append("*工作区删除的文件：" + it + "\n"));
//        return sb.toString();
//    }
    @SneakyThrows
    @GetMapping("github/log/{repoName}")
    public String gitLog(@PathVariable("repoName") String repoName) {

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

            PersonIdent authoIdent = commit.getAuthorIdent();
            sb.append("提交人：  " + authoIdent.getName() + "     <" + authoIdent.getEmailAddress() + ">\n");
            sb.append("版本号：  " + commit.getId().name() + "\n");
            sb.append("提交信息：  " + commit.getShortMessage() + "\n");
            sb.append("提交时间：  " + format.format(authoIdent.getWhen()) + "\n");
            sb.append("---------------------------------------------\n");
        }


        return sb.toString();

    }


    @SneakyThrows
    @GetMapping("github/files/{repoName}")
    public String gitFiles(@PathVariable("repoName") String repoName) {


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

        //如果想直接获取所有文件的话就是启动下面这个，这样的话他就不会显示文件夹了....
        //这里的话我们只是想展示表层目录 所以没有必要使用他
        //tw.setRecursive(true);

        while (tw.next()) {

            sb.append(new GitObject(tw,repository).toString());

        }

        return sb.toString();
    }

    /**
     * 如果是文件的话就返回详细信息，如果是目录的话就返回子目录，这里我选择的是作为两个方法来实现
     * @param repoName
     * @return
     */
    @SneakyThrows
    @GetMapping("github/files/{repoName}/blob/{brench}")
    public String readFiles(@PathVariable("repoName") String repoName, String path) {


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

        tw.setRecursive(true);
        while (tw.next()) {

            if(tw.getPathString().equals(path))
            {
                return  new GitObject(tw,repository).toString();
            }
        }
        return "找不到信息...";

    }

    private static AntPathMatcher antPathMatcher = new AntPathMatcher();

    @SneakyThrows
    @GetMapping("github/files/{repoName}/tree/{brench}")
    public String readDir(@PathVariable("repoName") String repoName,String path) {



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


        StringBuilder sb = new StringBuilder("文件目录：\n");

        while (tw.next()) {


            String pathString = tw.getPathString();
            //第一层精确匹配
            if(path.startsWith(pathString))
            {
                //如果完全一样
                if(path.equals(pathString))
                {

                    tw.enterSubtree();
                    while(tw.next())
                    {
                        if((tw.getPathString()+"/").startsWith(path))
                        {
                            sb.append(new GitObject(tw,repository).toString());
                        }
                        else
                        {
                            break;
                        }

                    }
                    break;
                }
                //如果只是子目录，进入，递归
                tw.enterSubtree();
            }

        }


        return sb.toString();
    }


}

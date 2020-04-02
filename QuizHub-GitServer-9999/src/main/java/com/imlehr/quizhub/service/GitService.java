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

import java.io.*;
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


        //准备一个钩子文件，来让以后每次客户提交了之后触发记录接口

        File postHook = new File(gitUrl + "/hooks/post-update");
        postHook.createNewFile();
        FileOutputStream outStream = new FileOutputStream(postHook);
        String s = "#!/bin/sh\n" +
                "\n" +
                "curl http://localhost:9999/lehr/github/addlog\n" +
                "\n" +
                "exec git update-server-info";
        outStream.write(s.getBytes());
        outStream.close();

        //把钩子设置为可执行的  TODO: 现在把钩子放到前面是为了解决http同步的问题，主要就是需要一次回调...
        postHook.setExecutable(true);


        String remoteLocal = "lehr@localhost:GitRepo/Lehr130/" + repoName + ".git";

        helloGit.push().setRemote(remoteLocal).call();


        //最后和bare仓库建立连接就好了
        return "仓库创建成功！\n" +
                "ssh链接是：" + remoteLocal +
                "\nhttp链接是：" + "http://localhost:9999/http/" + repoName + suffix;
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

        Repository repository = git.getRepository();

        TreeWalk tw = new TreeWalk(repository);

        //把 最新版本的树 的目录放入到TreeWalk里去准备遍历
        RevCommit current = log.call().iterator().next();
        tw.addTree(current.getTree());

        while (tw.next()) {
            sb.append(new GitObject(tw, repository, git).toString());
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
    public String onlineUpload(String repoName, String filename) {
        //先去目标地点创建一个文件

        //把这个文件上传了
        //创建本地仓库
        String temp = UUID.randomUUID().toString();


        String remoteLocal = "lehr@localhost:GitRepo/Lehr130/" + repoName + ".git";

        File directory = new File(prefix + temp);
        Git tempGit = Git.cloneRepository()
                .setURI(remoteLocal)
                .setDirectory(directory)
                .setCloneAllBranches(false)
                .call();


        File tempUploadFile = new File(prefix + temp + "/" + filename);
        tempUploadFile.createNewFile();
        FileOutputStream outStream = new FileOutputStream(tempUploadFile);
        String s = "# Uploading\nThis is web uploading temp file\n>From Lehr";
        outStream.write(s.getBytes());
        outStream.close();

        //git add . 是默认忽略空目录的！！！

        tempGit.add().addFilepattern(".").call();

        CommitCommand commitCommand = tempGit.commit().setMessage("WebUploading Test!").setAllowEmpty(true);
        commitCommand.call();

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
    public String onlineDelete(String repoName, String path) {

        //先去目标地点创建一个文件

        //把这个文件上传了
        //创建本地仓库
        String temp = UUID.randomUUID().toString();


        //先查询目录有没有，省的白做功
//        File target = new File(prefix+temp + "/"+ path);
//        if(!target.exists())
//        {
//            return "目标不存在";
//        }


        String remoteLocal = "lehr@localhost:GitRepo/Lehr130/" + repoName + ".git";

        File directory = new File(prefix + temp);
        Git tempGit = Git.cloneRepository()
                .setURI(remoteLocal)
                .setDirectory(directory)
                .setCloneAllBranches(false)
                .call();

        File deletarget = new File(prefix + temp + "/" + path);

        if (!deletarget.exists()) {
            return "文件不存在";
        }
        if (deletarget.isFile()) {
            deletarget.delete();
        } else {
            deleteDir(deletarget);
        }

        tempGit.add().setUpdate(true).addFilepattern(".").call();

        CommitCommand commitCommand = tempGit.commit().setMessage("delete Test!").setAllowEmpty(true);
        commitCommand.call();

        tempGit.push().setRemote(remoteLocal).call();

        //由于目录不是空目录，所以需要递归删除
        deleteDir(directory);

        return "删除完成，请检查";

    }

    @SneakyThrows
    public String addKey(String key) {
        String keyUrl = "/home/lehr/.ssh/authorized_keys";

        File auth = new File(keyUrl);

        //第二个参数表示要追加内容
        BufferedWriter bw = new BufferedWriter(new FileWriter(auth, true));

        //TODO 以后需要写一个匹配算法
        bw.write("\n" + key + "\n");

        //TODO 暂时没有考虑多线程问题
        bw.close();

        return "添加成功";

    }

}

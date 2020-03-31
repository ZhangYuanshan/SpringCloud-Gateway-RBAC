package com.imlehr.quizhub.controller;

import com.imlehr.quizhub.GitServerApplication;
import com.imlehr.quizhub.git.MyRevFilter;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.StopWalkException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author Lehr
 * @create: 2020-03-30
 */
@RestController
@RequestMapping("lehr")
public class GitWorkController {

    private final static String suffix = ".git";
    private final static String prefix = "/home/lehr/GitRepo/Lehr130/";
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SneakyThrows
    @GetMapping("github/create/{repoName}")
    public String git(@PathVariable("repoName") String repoName) {

        //创建本地仓库
        String workUrl = prefix + repoName;
        String gitUrl = workUrl + File.separator + suffix;
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
                            ConfigConstants.CONFIG_KEY_NAME, "Lehr's Test");

            build.getConfig().save();

        } catch (IOException e) {
            return "创建出问题了！";
        }

        File readMe = new File(workUrl + File.separator + "readme.md");

        readMe.createNewFile();

        FileOutputStream outStream = new FileOutputStream(readMe);
        outStream.write("# Hey Lehr\n welcome to my Git Server!".getBytes());
        outStream.close();


        Git git = Git.open(basic);

        git.add().addFilepattern(".").call();

        CommitCommand commitCommand = git.commit().setMessage(":tada:Init!").setAllowEmpty(true);
        commitCommand.call();

        //最后和bare仓库建立连接就好了
        return "仓库创建成功！ssh链接是：lehr@localhost:GitRepo/Lehr130/" + repoName;
    }

    @SneakyThrows
    @GetMapping("github/status/{repoName}")
    public String gitStatus(@PathVariable("repoName") String repoName) {

        //创建本地仓库
        String workUrl = prefix + repoName;
        String gitUrl = workUrl + File.separator + suffix;
        File dir = new File(gitUrl);
        if (!dir.exists()) {
            return "仓库不存在";
        }
        Git git = Git.open(dir);

        //返回的值都是相对工作区的路径，而不是绝对路径
        Status status = git.status().call();
        StringBuilder sb = new StringBuilder("修改记录：\n");
        //git add命令后会看到变化
        status.getAdded().forEach(it -> sb.append("*添加文件：" + it + "\n"));
        ///git rm命令会看到变化，从暂存区删除的文件列表
        status.getRemoved().forEach(it -> sb.append("*删除文件：" + it + "\n"));
        //修改的文件列表
        status.getModified().forEach(it -> sb.append("*修改文件：" + it + "\n"));
        //工作区新增的文件列表
        //status.getUntracked().forEach(it -> System.out.println("Untracked File :" + it));
        //冲突的文件列表
        //status.getConflicting().forEach(it -> System.out.println("Conflicting File :" + it));
        //工作区删除的文件列表
        //status.getMissing().forEach(it -> System.out.println("Missing File :" + it));
        return sb.toString();
    }

    @SneakyThrows
    @GetMapping("github/log/{repoName}")
    public String gitLog(@PathVariable("repoName") String repoName) {

        //创建本地仓库
        String workUrl = prefix + repoName;
        String gitUrl = workUrl + File.separator + suffix;
        File dir = new File(gitUrl);
        if (!dir.exists()) {
            return "仓库不存在";
        }
        Git git = Git.open(dir);


        Iterable<RevCommit> results = git.log().setRevFilter(new MyRevFilter()).call();
        StringBuilder sb = new StringBuilder("日志如下：\n");

        results.forEach(commit -> {
            PersonIdent authoIdent = commit.getAuthorIdent();
            sb.append("提交人：  " + authoIdent.getName() + "     <" + authoIdent.getEmailAddress() + ">\n");
            sb.append("提交SHA1：  " + commit.getId().name()+"\n");
            sb.append("提交信息：  " + commit.getShortMessage()+"\n");
            sb.append("提交时间：  " + format.format(authoIdent.getWhen())+"\n");
        });
        return sb.toString();

    }


    @SneakyThrows
    @GetMapping("github/files/{repoName}")
    public String gitFiles(@PathVariable("repoName") String repoName) {

        //创建本地仓库
        String workUrl = prefix + repoName;
        String gitUrl = workUrl + File.separator + suffix;
        File dir = new File(gitUrl);
        if (!dir.exists()) {
            return "仓库不存在";
        }

        Git git = Git.open(dir);

        StringBuilder sb = new StringBuilder("文件目录：\n");

        Iterable<RevCommit> logs = git.log().call();
        int k = 0;
        for (RevCommit commit : logs) {
            String commitID = commit.getName();
            if (commitID != null && !commitID.isEmpty())
            {
                LogCommand logs2 = git.log().all();
                Repository repository = logs2.getRepository();
                TreeWalk tw = new TreeWalk(repository);
                tw.setRecursive(true);
                RevCommit commitToCheck = commit;
                tw.addTree(commitToCheck.getTree());
                for (RevCommit parent : commitToCheck.getParents())
                {
                    tw.addTree(parent.getTree());
                }
                while (tw.next())
                {
                    int similarParents = 0;
                    for (int i = 1; i < tw.getTreeCount(); i++)
                    {
                        if (tw.getFileMode(i) == tw.getFileMode(0) && tw.getObjectId(0).equals(tw.getObjectId(i)))
                        {
                            similarParents++;
                        }
                    }
                    if (similarParents == 0)
                    {
                        sb.append("--->\t" + tw.getNameString()+"\n");
                    }
                }
            }
        }

        return sb.toString();
    }
}

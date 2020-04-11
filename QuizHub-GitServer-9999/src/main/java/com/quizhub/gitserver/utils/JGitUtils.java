package com.quizhub.gitserver.utils;

import lombok.SneakyThrows;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Lehr
 * @create: 2020-04-06
 */
public class JGitUtils {

    private static final String PREFIX = "/home/"+System.getProperty("user.name")+"/GitRepo/";

    @SneakyThrows
    public static Boolean gitInit(Boolean isBare, File gitRepo, String owner) {

        if (!isBare) {
            Git.init().setGitDir(gitRepo).setDirectory(gitRepo.getParentFile()).call();
        }
        else
        {
            Git.init().setGitDir(gitRepo).call();
        }


        try (Repository build = new RepositoryBuilder().setGitDir(gitRepo).setMustExist(true).build()) {

            build.getConfig().setString(ConfigConstants.CONFIG_USER_SECTION, null, ConfigConstants.CONFIG_KEY_NAME, owner);

            build.getConfig().save();

        } catch (IOException e) {
            return false;
        }

        return true;

    }


    @SneakyThrows
    public static Boolean addCommit(Git git,String message)
    {

        git.add().addFilepattern(".").call();

        CommitCommand commitCommand = git.commit().setMessage(message).setAllowEmpty(true);

        commitCommand.call();

        return true;
    }

    @SneakyThrows
    public static File mkdir(String url)
    {
        File directory = new File(PREFIX + url);
        if(!directory.exists()){
            directory.mkdirs();
        }
        return directory;
    }

    public static File getFile(String url)
    {
        return new File(PREFIX+url);
    }


    @SneakyThrows
    public static File createFile(String url,String info)
    {
        //准备一个钩子文件，来让以后每次客户提交了之后触发记录接口
        File file = new File(url);
        if(!file.exists())
        {
            file.createNewFile();
        }
        FileOutputStream outStream = new FileOutputStream(file);
        outStream.write(info.getBytes());
        outStream.close();
        return file;
    }

    public static String getRepoPath(String owner, String repoName)
    {
        return PREFIX + owner +File.separator+ repoName;
    }

}

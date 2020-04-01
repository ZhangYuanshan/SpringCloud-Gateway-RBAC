package com.imlehr.quizhub.git;

import lombok.SneakyThrows;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Lehr
 * @create: 2020-03-30
 */
@RestController
@RequestMapping("gitserver")
public class GitController {

    @SneakyThrows
    @GetMapping("git")
    public String git()
    {

        //创建本地仓库
        String workUrl = "/home/lehr/Lerie";
        String gitUrl = workUrl + "/.git";
        File dir = new File(gitUrl);
        Git.init().setGitDir(dir)
                .setDirectory(dir.getParentFile()).call();


        //配置本地仓库
        Repository build = null;
        try {
            build = new RepositoryBuilder().setGitDir(new File(gitUrl)).setMustExist(true)
                    .build();
            build.getConfig().setString(ConfigConstants.CONFIG_USER_SECTION, null, ConfigConstants.CONFIG_KEY_NAME,
                    "xxxx");
            build.getConfig().save();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("fuck!");
        } finally {
            if (null != build) {
                build.close();
            }
        }

        File readMe = new File(workUrl+File.separator+"readme.md");

        readMe.createNewFile();

        FileOutputStream outStream = new FileOutputStream(readMe);
        outStream.write("#Hey Lehr".getBytes());
        outStream.close();



        Git git = Git.open(new File(gitUrl));
        // 相当与git add -A添加所有的变更文件git.add().addFilepattern("*.java")这种形式是不支持的
        git.add().addFilepattern(".").call();
        // 添加目录，可以把目录下的文件都添加到暂存区
        //git.add().addFilepattern("src/main/java/").call();
        //jgit当前还不支持模式匹配的方式，例如*.java


        CommitCommand commitCommand = git.commit().setMessage("HeyLerie!").setAllowEmpty(true);
        commitCommand.call();

        return "done!";
    }
//
//    @SneakyThrows
//    @GetMapping("/{repoName}/info/refs")
//    public byte[] getRefs(String service, @PathVariable("repoName")String repoName)
//    {
//
//
//        //获取仓库控制地址
//        String repo = root + repoName.substring(0,repoName.length()-4) + suffix;
//
//        Git git = Git.open(new File(repo));
//
//
//        String masterUri = repo + File.separator + "refs" + File.separator + "master" + File.separator + "master";
//
//
//
//        FileInputStream fileInputStream = new FileInputStream(masterUri);
//        int length = 0 ;
//        //建立缓存数组，缓存数组的大小一般都是1024的整数倍，理论上越大效率越好
//        byte[] buf = new byte[1024];
//        while((length = fileInputStream.read(buf))!=-1){
//            System.out.print(new String(buf,0 ,length));
//        }
//        fileInputStream.close();
//
//        return buf;
//
////
////        if(service.equals("git-receive-pack"))
////        {
////
////            //是push操作
////            return "push+"+repo;
////        }
////        if(service.equals("git-upload-pack"))
////        {
////            //是pull或者clone操作
////            return "pull"+repo;
////        }
//
////        return "fuck";
//    }
//
//
//    @SneakyThrows
//    @GetMapping("/{repoName}/HEAD")
//    public byte[] getHead(String service, @PathVariable("repoName")String repoName)
//    {
//
//
}

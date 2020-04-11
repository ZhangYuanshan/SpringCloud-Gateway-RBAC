package com.quizhub.gitserver.javabean;

import lombok.SneakyThrows;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Lehr
 * @create: 2020-04-01
 */
public class GitObject {

    private String filename;
    private String path;
    private Boolean isDir;
    private String message;
    private String date;
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SneakyThrows
    public GitObject(TreeWalk tw, Repository repository, Git git)
    {
        //git log filename   log只能被调用一次，所以这里需要重新git.log.call
        RevCommit currentCommit = git.log().addPath(tw.getPathString()).call().iterator().next();

        //只需要返回第一行提示就可以了
        message = currentCommit.getShortMessage();

        PersonIdent authoIdent = currentCommit.getAuthorIdent();
        Date when = authoIdent.getWhen();
        date = format.format(when);


        filename = tw.getNameString();
        path = tw.getPathString();
        isDir = tw.isSubtree();
    }


    @Override
    public String toString() {

        String logo = "[x]文件:\t";

        if(isDir)
        {
            logo = "[+]目录:\t";
        }

        return "-----------------------------------------------------------------------------------------------------------\n"+
                logo + filename +"\t\t\t"+message+"\t\t"+date+"\n";

    }
}

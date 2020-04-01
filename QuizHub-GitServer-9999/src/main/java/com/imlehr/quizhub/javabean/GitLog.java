package com.imlehr.quizhub.javabean;

import lombok.SneakyThrows;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author Lehr
 * @create: 2020-04-01
 */
public class GitLog {

    private String author;
    private String email;
    private String commitId;
    private String time;
    private String message;
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public GitLog(RevCommit commit)
    {
        PersonIdent authoIdent = commit.getAuthorIdent();
        author = authoIdent.getName();
        email = authoIdent.getEmailAddress();
        commitId = commit.getId().name();
        message = commit.getFullMessage();
        time = format.format(authoIdent.getWhen());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("提交人：  " + author + "     <" + email + ">\n");
        sb.append("提交时间：  " + time + "\n");
        sb.append("版本号：  " + commitId + "\n");
        sb.append("提交信息：  " + message + "\n");
        sb.append("---------------------------------------------\n");
        return sb.toString();
    }
}

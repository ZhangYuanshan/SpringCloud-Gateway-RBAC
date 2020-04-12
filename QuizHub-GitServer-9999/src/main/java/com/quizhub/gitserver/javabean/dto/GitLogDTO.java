package com.quizhub.gitserver.javabean.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author Lehr
 * @create: 2020-04-01
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class GitLogDTO{

    private String author;
    private String email;
    private String commitId;
    private String time;
    private String message;
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public GitLogDTO(RevCommit commit)
    {
        PersonIdent authoIdent = commit.getAuthorIdent();
        author = authoIdent.getName();
        email = authoIdent.getEmailAddress();
        commitId = commit.getId().name();
        message = commit.getFullMessage();
        time = format.format(authoIdent.getWhen());
    }

}

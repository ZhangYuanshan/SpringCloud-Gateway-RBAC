package com.quizhub.gitserver.javabean.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Lehr
 * @create: 2020-04-01
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class GitFileInfoDTO{

    private String filename;
    private String path;
    private Boolean isDir;
    private String message;
    private String date;
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SneakyThrows
    public GitFileInfoDTO(TreeWalk tw, Repository repository, Git git)
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


}

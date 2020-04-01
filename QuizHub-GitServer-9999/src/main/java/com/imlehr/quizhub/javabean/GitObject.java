package com.imlehr.quizhub.javabean;

import lombok.SneakyThrows;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author Lehr
 * @create: 2020-04-01
 */
public class GitObject {

    private String filename;
    private String path;
    private Boolean isDir;

    @SneakyThrows
    public GitObject(TreeWalk tw,Repository repository)
    {
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
                logo + filename +"\t\t\t" +path+"\n";

    }
}

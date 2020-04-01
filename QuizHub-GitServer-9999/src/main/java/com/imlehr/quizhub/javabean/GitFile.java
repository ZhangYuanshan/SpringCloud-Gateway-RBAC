package com.imlehr.quizhub.javabean;

import lombok.SneakyThrows;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.TreeWalk;

/**
 * @author Lehr
 * @create: 2020-04-01
 */
public class GitFile {

    private String filename;
    private ObjectId objectId;
    private String content;



    @SneakyThrows
    public GitFile(TreeWalk tw, Repository repository)
    {
        if(tw.isSubtree())
        {
            throw new Exception("程序出错");
        }

        objectId = tw.getObjectId(0);
        ObjectLoader loader = repository.open(objectId);
        content = new String(loader.getBytes());
        filename = tw.getNameString();

    }


    @Override
    public String toString() {

        return "文件名："+filename+"\n-----------------------------------------------------------------------------------------\n"
                +content;
    }
}

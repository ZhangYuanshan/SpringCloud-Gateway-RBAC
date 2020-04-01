package com.imlehr.quizhub.javabean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.w3c.dom.traversal.TreeWalker;

/**
 * @author Lehr
 * @create: 2020-04-01
 */
public class GitObject {

    private String filename;
    private ObjectId objectId;
    private String path;
    private String content;
    private Boolean isDir;


    @SneakyThrows
    public GitObject(TreeWalk tw,Repository repository)
    {
        objectId = tw.getObjectId(0);
        ObjectLoader loader = repository.open(objectId);
        content = new String(loader.getBytes());
        filename = tw.getNameString();
        path = tw.getPathString();
        isDir = tw.isSubtree();
    }


    @Override
    public String toString() {
        String out = null;
        String type;
        if(isDir)
        {
            type = "tree";
            out = "--------------------------------\n";
        }
        else
        {
            type = "blob";
            out = content+"\n--------------------------------\n";
        }
        return "----> Filename:\t"+filename+"\n"+"Path:\t"+path+"\nObjectId:\t"
                +objectId+"\nisDir:\t"+isDir+"\nType:\t"+type+"\n"+out;
    }
}

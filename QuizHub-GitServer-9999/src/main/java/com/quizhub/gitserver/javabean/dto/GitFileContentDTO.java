package com.quizhub.gitserver.javabean.dto;

import com.quizhub.common.javabean.MyException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.Serializable;

/**
 * @author Lehr
 * @create: 2020-04-01
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class GitFileContentDTO{

    private String filename;
    private byte[] content;



    @SneakyThrows
    public GitFileContentDTO(TreeWalk tw, Repository repository)
    {
        if(tw.isSubtree())
        {
            throw new MyException("程序出错，这不是文件！","2220");
        }

        ObjectLoader loader = repository.open(tw.getObjectId(0));
        content = loader.getBytes();
        filename = tw.getNameString();

    }

}

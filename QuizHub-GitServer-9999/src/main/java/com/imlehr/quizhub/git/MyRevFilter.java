package com.imlehr.quizhub.git;

import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.StopWalkException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;

import java.io.IOException;

/**
 * @author Lehr
 * @create: 2020-03-31
 */
public class MyRevFilter extends RevFilter {
    @Override
    public boolean include(RevWalk walker, RevCommit cmit)
            throws StopWalkException, MissingObjectException, IncorrectObjectTypeException, IOException {
        return cmit.getAuthorIdent().getName().equals("xxxxx dsd");
    }

    @Override
    public RevFilter clone() {
        return this;
    }
}

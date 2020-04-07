package com.imlehr.quizhub.dao;

import com.imlehr.quizhub.javabean.bo.UserBO;
import com.imlehr.quizhub.javabean.po.UserPO;

/**
 * @author Lehr
 * @create: 2020-03-30
 */
public interface UserMapper {

    UserBO getUserByName(String username);

    UserBO getUserByGithubId(String githubId);

    UserBO createUser(UserPO user);

}

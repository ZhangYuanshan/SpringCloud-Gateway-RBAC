package com.quizhub.uaa.dao;

import com.quizhub.uaa.javabean.bo.UserBO;
import com.quizhub.uaa.javabean.po.UserPO;

/**
 * @author Lehr
 * @create: 2020-03-30
 */
public interface UserMapper {

    UserBO getUserByName(String username);

    UserBO getUserByGithubId(String githubId);

    UserBO createUser(UserPO user);

}

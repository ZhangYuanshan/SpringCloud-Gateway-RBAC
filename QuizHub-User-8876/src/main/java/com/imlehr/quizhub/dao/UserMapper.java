package com.imlehr.quizhub.dao;

import com.imlehr.quizhub.javabean.po.UserPO;

/**
 * @author Lehr
 * @create: 2020-04-02
 */
public interface UserMapper {

    UserPO getUserInfoById(String userId);

}

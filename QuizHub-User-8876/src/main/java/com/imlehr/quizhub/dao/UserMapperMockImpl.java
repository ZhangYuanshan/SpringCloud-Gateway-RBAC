package com.imlehr.quizhub.dao;

import com.imlehr.quizhub.javabean.po.UserPO;
import org.springframework.stereotype.Component;

/**
 * @author Lehr
 * @create: 2020-04-02
 */
@Component
public class UserMapperMockImpl implements UserMapper{

    @Override
    public UserPO getUserInfoById(String userId){
        return null;
    }

}

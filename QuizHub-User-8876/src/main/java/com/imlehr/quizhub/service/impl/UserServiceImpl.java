package com.imlehr.quizhub.service.impl;

import com.imlehr.quizhub.dao.UserMapper;
import com.imlehr.quizhub.javabean.po.UserPO;
import com.imlehr.quizhub.javabean.vo.UserVO;
import com.imlehr.quizhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Lehr
 * @create: 2020-04-02
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper mapper;

    @Override
    public UserVO getUserIntro(String userId) {

        //获取po
        UserPO userPO = mapper.getUserInfoById(userId);

        //模型转化并返回
        return new UserVO(userPO);

    }
}

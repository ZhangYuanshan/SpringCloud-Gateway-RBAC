package com.imlehr.quizhub.service;

import com.imlehr.quizhub.javabean.vo.UserVO;

/**
 * @author Lehr
 * @create: 2020-04-02
 */
public interface UserService {

    /**
     * 通过用户id查询获取用户的基本信息
     * @param userId
     * @return
     */
    UserVO getUserIntro(String userId);

}

package com.imlehr.quizhub.dao;

import com.imlehr.quizhub.javabean.vo.UserPO;
import org.springframework.stereotype.Component;

/**
 * @author Lehr
 * @create: 2020-04-02
 */
@Component
public interface UserMapper {

    UserPO getUserInfoById(String userId);

}

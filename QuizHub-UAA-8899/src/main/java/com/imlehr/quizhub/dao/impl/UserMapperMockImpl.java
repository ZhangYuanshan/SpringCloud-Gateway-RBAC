package com.imlehr.quizhub.dao.impl;

import com.imlehr.quizhub.dao.UserMapper;
import com.imlehr.quizhub.javabean.po.UserBO;
import com.imlehr.quizhub.javabean.po.UserDTO;
import com.imlehr.quizhub.javabean.po.UserPO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lehr
 * @create: 2020-03-30
 */
@Component
public class UserMapperMockImpl implements UserMapper {

    private static Map<String, UserPO> userPool = new HashMap<String, UserPO>();

    static{
        //密码就是lerie bcr加密看  String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        userPool.put("1",new UserPO().setUsername("Lehr")
                .setUserId("1").setGithubId("45219850").setBio("Hey Lehr")
                .setPassword("$2a$10$f/Wa6ESfZEGBvu/R3OjiveRJRaZRqC1ON5cAHSl2Ym/E5VQeB/aPy"));
    }



    @Override
    public UserBO getUserByName(String username) {
        UserBO user = null;
        for (UserPO u : userPool.values()) {
            if(u.getUsername().equals(username))
            {
                user  = new UserBO().setPassword(u.getPassword())
                        .setUsername(u.getUsername())
                        .setUserId(u.getUserId())
                        .setGithubId(u.getGithubId());
            }
        }
        return user;
    }

    @Override
    public UserBO getUserByGithubId(String githubId) {
        UserBO user = null;
        for (UserPO u : userPool.values()) {
            if(u.getGithubId().equals(githubId))
            {
                user  = new UserBO().setPassword(u.getPassword())
                        .setUsername(u.getUsername())
                        .setUserId(u.getUserId())
                        .setGithubId(u.getGithubId());
            }
        }
        return user;
    }

    @Override
    public UserBO createUser(UserPO user) {
        String k = String.valueOf((int) Math.random());
        userPool.put(k,user.setUserId(k));
        return new UserBO().setPassword(user.getPassword())
                .setUsername(user.getUsername())
                .setUserId(user.getUserId())
                .setGithubId(user.getGithubId());
    }

}

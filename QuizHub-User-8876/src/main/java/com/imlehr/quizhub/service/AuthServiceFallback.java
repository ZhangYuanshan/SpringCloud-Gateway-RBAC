package com.imlehr.quizhub.service;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lehr
 * @create: 2020-03-23
 */
@Component
public class AuthServiceFallback implements AuthService {


    @Override
    public Object passwordLogin(@RequestParam("username")String username,
                         @RequestParam("password")String password,
                         @RequestParam("grant_type")String grant_type,
                         @RequestParam("client_id")String client_id,
                         @RequestParam("client_secret")String client_secret){
        return null;
    }

    @Override
    public Object refresh(@RequestParam("grant_type")String grant_type,
                          @RequestParam("refresh_token")String refresh_token) {
        return null;
    }

}





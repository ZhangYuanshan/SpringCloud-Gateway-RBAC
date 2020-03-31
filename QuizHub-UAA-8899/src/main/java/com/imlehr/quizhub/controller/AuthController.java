package com.imlehr.quizhub.controller;

import com.imlehr.quizhub.javabean.po.ResponseMsg;
import com.imlehr.quizhub.javabean.po.UserBO;
import com.imlehr.quizhub.javabean.po.UserDTO;
import com.imlehr.quizhub.service.AccountService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Lehr
 * @create: 2020-03-27
 */
@RestController
public class AuthController {


    @Autowired
    private AccountService accountService;


    @PostMapping("/auth/login")
    public ResponseMsg passwordLogin(String username, String password)
    {
        return accountService.login(username,password);
    }

    /**
     * 注册成功，返回登录token
     * @param user
     * @return
     */
    @PostMapping("/auth/register")
    public ResponseMsg register(UserDTO user)
    {
        return accountService.register(user);
    }


    /**
     * 认证成功，返回登录token，如果账号没有绑定就新建一个账号
     * @param code
     * @return
     */
    @PostMapping("/oauth/github")
    public ResponseMsg githubOAuth(String code)
    {
        return accountService.GithubLogin(code);
    }




}

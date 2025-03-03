package com.quizhub.uaa.controller;

import com.quizhub.common.javabean.ResponseMsg;
import com.quizhub.uaa.javabean.dto.UserDTO;
import com.quizhub.uaa.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Lehr
 * @create: 2020-03-27
 */
@RestController
@RequestMapping("auth")
@Api(tags = "账户认证接口")
public class AccountAuthController {


    @Autowired
    private AccountService accountService;

    @ApiOperation(value = "用户登录",notes = "对外暴露的接口，通过账号密码登录")
    @PostMapping("login")
    public ResponseMsg passwordLogin(String username, String password)
    {
        return accountService.login(username,password);
    }

    /**
     * 注册成功，返回登录token
     * @param user
     * @return
     */
    @ApiOperation(value = "用户注册",notes = "对外暴露的接口，填写基本信息注册一个新账号")
    @PostMapping("register")
    public ResponseMsg register(UserDTO user)
    {
        return accountService.register(user);
    }


    /**
     * 认证成功，返回登录token，如果账号没有绑定就新建一个账号
     * @param code
     * @return
     */
    @ApiOperation(value = "Github登录",notes = "对外暴露的接口，从前端获取返回码，如果没有账号就会创建一个，目前存在密码问题")
    @PostMapping("github")
    public ResponseMsg githubOAuth(String code)
    {
        return accountService.GithubLogin(code);
    }




}

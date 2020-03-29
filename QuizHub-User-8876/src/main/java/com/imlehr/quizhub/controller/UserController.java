package com.imlehr.quizhub.controller;

import com.imlehr.quizhub.javabean.pojo.MyException;
import com.imlehr.quizhub.service.AuthService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lehr
 * @create: 2020-03-27
 */
@RestController
@RequestMapping("users")
public class UserController {


    @Autowired
    private AuthService authService;

    @PostMapping("login")
    public Object passwordLogin(String username, String password)
    {
        String grant_type = "password";
        String client_id = "Lehr";
        String client_secret = "Lehr's_Secret";
        return  authService.passwordLogin(username, password, grant_type, client_id, client_secret);
    }




    @GetMapping("test")
    public String test()
    {
        return "Hey Lehr!";
    }


    @SneakyThrows
    @GetMapping("1")
    public String ex()
    {
        throw new Exception("FUck you!");
    }


    @SneakyThrows
    @GetMapping("2")
    public String ex2()
    {
        throw new MyException("我想让你看到的错误","405",500);
    }


}

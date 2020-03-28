package com.imlehr.quizhub.controller;

import com.imlehr.quizhub.service.AuthService;
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

}

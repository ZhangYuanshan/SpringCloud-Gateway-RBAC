package com.imlehr.quizhub.controller;

import com.imlehr.quizhub.javabean.pojo.MyException;
import com.imlehr.quizhub.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author Lehr
 * @create: 2020-03-27
 */
@RestController
@RequestMapping("users")
public class UserController {


    @Autowired
    private UserService userService;


    @GetMapping("/intros/{userId}")
    public String getUserIntro(@PathVariable("userId")String userId)
    {
        return "Hey Lehr!";
    }



}

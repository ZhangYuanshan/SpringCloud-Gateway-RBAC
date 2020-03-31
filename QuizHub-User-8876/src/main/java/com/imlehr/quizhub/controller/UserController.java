package com.imlehr.quizhub.controller;

import com.imlehr.quizhub.javabean.pojo.MyException;
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





    @GetMapping("test")
    public String test()
    {
        return "Hey Lehr!";
    }







    @SneakyThrows
    @GetMapping("1")
    public String ex1()
    {
        throw new Exception("F**k you!");
    }

    @SneakyThrows
    @GetMapping("2")
    public String ex2()
    {
        throw new MyException("我想让你看到的错误","1000",401);
    }


}

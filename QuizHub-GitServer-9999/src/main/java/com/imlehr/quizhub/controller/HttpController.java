package com.imlehr.quizhub.controller;

import com.imlehr.quizhub.service.GitService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Lehr
 * @create: 2020-03-30
 */
@RestController
@RequestMapping("http")
public class HttpController {


    @SneakyThrows
    @GetMapping("/**")
    public byte[] dumb(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        String path = requestURI.substring(5);

        File file = new File("/home/lehr/GitRepo/Lehr130" + path);
        System.out.println(file.getAbsolutePath());
        Long len = file.length();
        byte[] fileContent = new byte[len.intValue()];
        FileInputStream in = new FileInputStream(file);
        in.read(fileContent);

        return fileContent;

    }

}

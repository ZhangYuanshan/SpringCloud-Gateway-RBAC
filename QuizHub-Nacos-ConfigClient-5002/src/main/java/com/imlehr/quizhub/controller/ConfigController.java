package com.imlehr.quizhub.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lehr
 * @create: 2020-03-25
 */
@RestController
@RefreshScope
public class ConfigController {

    @Value("${config.info}")
    private String info;

    @GetMapping("/config/info")
    public String configInfo()
    {
        return info;
    }



}

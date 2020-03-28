package com.imlehr.quizhub.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.imlehr.quizhub.handler.FlowLimitHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lehr
 * @create: 2020-03-25
 */
@RestController
public class FlowLimitController {

    @GetMapping("/a")
    public String testA()
    {
        return "Hey Lehr, It's A!";
    }

    @GetMapping("/b")
    public String testB()
    {
        return "Hey Lehr, It's B!";
    }

    @GetMapping("/hotspot")
    @SentinelResource(value="LehrsCallback",blockHandlerClass = FlowLimitHandler.class,blockHandler = "blockHotspot")
    public String hotspot(String p1,String p2)
    {
        return "wow ! It's cool!";
    }


}

package com.imlehr.quizhub.controller;
import com.imlehr.quizhub.javabean.vo.Guitar;
import com.imlehr.quizhub.service.GuitarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Lehr
 * @create: 2020-03-22
 */
@RequestMapping("/guitars")
@RestController
public class GuitarController {

    @Autowired
    private GuitarService guitarService;

    @Autowired
    private DiscoveryClient client;

    @GetMapping("")
    //@HystrixCommand(fallbackMethod = "hystrixBuyGuitar")
    public Guitar buyGuitar(Integer money)
    {
        System.out.println("Hey!");
        if(money==null)
        {
            throw new NullPointerException();
        }
        return guitarService.buyGuitar(money);
    }

    public Guitar hystrixBuyGuitar(Integer money)
    {
        return new Guitar("Les Paul","Gibson");
    }

    public Object discovery()
    {
        //获取所有微服务
        List<String> list = client.getServices();
        System.out.println(list);

        //用名字找一个微服务
        List<ServiceInstance> instanceList = client.getInstances("GuitarProvider-8001");

        System.out.println(instanceList);

        return  this.client;
    }





}

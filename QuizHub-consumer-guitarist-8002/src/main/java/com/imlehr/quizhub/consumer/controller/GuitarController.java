package com.imlehr.quizhub.consumer.controller;

import com.imlehr.quizhub.javabean.vo.Guitar;
import com.imlehr.quizhub.service.GuitarClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lehr
 * @create: 2020-03-23
 */
@RequestMapping("consumers/")
@RestController
public class GuitarController {

    private static final String REST_URL_PREFIX = "http://GUITARPROVIDER";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("guitars")
    public Guitar buyGuitar(Integer money)
    {

        Map<String, Object> params = new HashMap<>(2);
        params.put("money", money);
        return restTemplate.getForObject(REST_URL_PREFIX+"/guitars?money={money}",Guitar.class,params);
    }

    @Autowired
    private GuitarClientService clientService;

    @GetMapping("feignGuitars")
    public Guitar buyGuitarFeign(Integer money)
    {
        return clientService.buyGuitar(money);
    }


}

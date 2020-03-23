package com.imlehr.quizhub.service;

import com.imlehr.quizhub.javabean.vo.Guitar;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lehr
 * @create: 2020-03-23
 */
@FeignClient(value="GuitarProvider",fallbackFactory = GuitarClientServiceFallbackFactory.class)
public interface GuitarClientService {

    @GetMapping("/guitars")
    Guitar buyGuitar(@RequestParam("money") Integer money);

}
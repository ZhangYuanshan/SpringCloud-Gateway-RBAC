package com.imlehr.quizhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * @author Lehr
 * @create: 2020-03-25
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class SentinalClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(SentinalClientApplication.class,args);
    }

}

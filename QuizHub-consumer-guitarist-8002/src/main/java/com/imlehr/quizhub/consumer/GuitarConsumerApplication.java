package com.imlehr.quizhub.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Lehr
 * @create: 2020-03-22
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.imlehr.quizhub"})
@ComponentScan(basePackages = "com.imlehr.quizhub")
@EnableEurekaClient
public class GuitarConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuitarConsumerApplication.class,args);
    }

}

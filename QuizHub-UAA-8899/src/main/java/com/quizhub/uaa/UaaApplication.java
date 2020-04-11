package com.quizhub.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Lehr
 * @create: 2020-03-27
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.quizhub")
@EnableFeignClients(basePackages = {"com.quizhub.uaa"})
public class UaaApplication {
    public static void main(String[] args) {
        SpringApplication.run(UaaApplication.class,args);
    }
}

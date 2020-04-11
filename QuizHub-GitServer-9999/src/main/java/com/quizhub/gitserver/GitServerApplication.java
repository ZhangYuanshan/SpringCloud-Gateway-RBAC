package com.quizhub.gitserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Lehr
 * @create: 2020-03-25
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.quizhub")
@EnableFeignClients(basePackages = {"com.quizhub.gitserver"})
public class GitServerApplication{
    public static void main(String[] args) {
        SpringApplication.run(GitServerApplication.class, args);
    }

}

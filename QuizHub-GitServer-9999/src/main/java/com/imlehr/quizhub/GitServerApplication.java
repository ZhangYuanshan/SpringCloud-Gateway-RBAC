package com.imlehr.quizhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Lehr
 * @create: 2020-03-25
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GitServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GitServerApplication.class, args);
    }

}

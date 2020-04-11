package com.quizhub.rbac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Lehr
 * @create: 2020-03-25
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.quizhub")
public class RBACApplication {
    public static void main(String[] args) {
        SpringApplication.run(RBACApplication.class, args);
    }

}

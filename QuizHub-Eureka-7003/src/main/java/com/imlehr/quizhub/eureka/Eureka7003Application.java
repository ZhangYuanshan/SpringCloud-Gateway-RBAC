package com.imlehr.quizhub.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author Lehr
 * @create: 2020-03-22
 */

@SpringBootApplication
@EnableEurekaServer
public class Eureka7003Application {
    public static void main(String[] args) {
        SpringApplication.run(Eureka7003Application.class,args);
    }
}

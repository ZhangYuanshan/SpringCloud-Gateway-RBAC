package com.quizhub.logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Lehr
 * @create: 2020-04-12
 */
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan("com.quizhub")
@EnableFeignClients(basePackages = {"com.quizhub.logger"})
public class LoggerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoggerApplication.class,args);
    }
}

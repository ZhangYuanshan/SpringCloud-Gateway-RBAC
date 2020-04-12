package com.quizhub.repository;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Lehr
 * @create: 2020-04-10
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableAsync
@ComponentScan("com.quizhub")
@EnableFeignClients(basePackages = {"com.quizhub.repository"})
public class RepoApplication {
    public static void main(String[] args) {
        SpringApplication.run(RepoApplication.class,args);
    }
}

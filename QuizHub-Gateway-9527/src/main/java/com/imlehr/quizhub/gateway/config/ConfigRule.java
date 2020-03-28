package com.imlehr.quizhub.gateway.config;

import com.imlehr.quizhub.gateway.bean.AuthRules;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ConfigRule {

    @Bean
    public AuthRules authRules()
    {
        return AuthRules.getBuilder()
                .antMatchers("/users/test").authenticated().hasAuthority("ROLE_USER")
                .and()
                .antMatchers("/order/**").permitAll()
                .and()
                .antMatchers("/security/**").hasAuthority("admin")
                .build();

    }



}
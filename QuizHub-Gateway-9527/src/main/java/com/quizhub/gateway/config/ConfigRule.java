package com.quizhub.gateway.config;

import com.quizhub.gateway.bean.AuthRules;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lehr
 * 配置你的规则
 */
@Configuration
public class ConfigRule {

    @Bean
    public AuthRules authRules()
    {
        return AuthRules.getBuilder()
                .antMatchers("/repos").authenticated()
                .and()
                .antMatchers("/repos/**").checkAuthority("GET","DELETE","POST","PUT")
                .and()
                .antMatchers("/order/**").authenticated()
                .build();

    }



}
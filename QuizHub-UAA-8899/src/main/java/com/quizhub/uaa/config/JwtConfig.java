package com.quizhub.uaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @author Lehr
 * @create: 2020-03-27
 * 令牌配置
 */
@Configuration
public class JwtConfig {


    private final static String SIGNING_KEY = "HeyLehr!!";

    /**
     * 配置存储策略，由于是jwt，就不用本地存储了
     * @return
     */
    @Bean
    public TokenStore tokenStore()
    {
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * 配置好jwt生成策略
     * @return
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter()
    {
        JwtAccessTokenConverter converter = new JwtConverter();
        converter.setSigningKey(SIGNING_KEY);
        return converter;
    }


}

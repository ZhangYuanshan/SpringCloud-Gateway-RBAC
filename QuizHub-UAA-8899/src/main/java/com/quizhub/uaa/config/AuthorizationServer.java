package com.quizhub.uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

/**
 * @author Lehr
 * @create: 2020-03-27
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;


    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;


    @Bean
   public AuthorizationServerTokenServices tokenServices(){
       DefaultTokenServices service = new DefaultTokenServices();
       service.setClientDetailsService(clientDetailsService);
       service.setSupportRefreshToken(true);
       service.setTokenStore(tokenStore);


       // 令牌增强
       TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
       tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
       service.setTokenEnhancer(tokenEnhancerChain);

       // 令牌有效期，两小时
       service.setAccessTokenValiditySeconds(7200);
       // 刷新令牌，3天
       service.setRefreshTokenValiditySeconds(259200);
       return service;
   }


   @Autowired
   private AuthorizationCodeServices authorizationCodeServices;

   //认证管理器

   @Autowired
   private AuthenticationManager authenticationManager;

    /**
     * 授权码的使用模式，暂时采用内存，不过目前暂时不支持用授权码登录了
     * @return
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(){
         return new InMemoryAuthorizationCodeServices();
    }








    //客户端用户配置
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("Lehr")  //客户端名字
                .secret(new BCryptPasswordEncoder().encode("Lehr's_Secret"))    //客户端秘钥
                .resourceIds("QuizHub")     //资源id,多个
                .authorizedGrantTypes("authorization_code",
                        "password",
                        "client_credentials",
                        "implict",          //各种允许的授权模式加上一个refresh token
                        "refresh_token")
                .scopes("all")  //允许授权的范围
                .autoApprove(false)     //false`就会跳转到授权页面
                .redirectUris("https://imlehr.com");   //回调地址，发请求的时候必须和这里注册的一样才可以,多了http都不行
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .authorizationCodeServices(authorizationCodeServices)
                .tokenServices(tokenServices())
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }


    //怎么去访问断点
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")          // oauth/token这个接口是公开的
                .checkTokenAccess("permitAll()")           //oauth/check_token公开
                .allowFormAuthenticationForClients();  //允许表单认证

    }

}

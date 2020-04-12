package com.quizhub.gateway.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.quizhub.gateway.bean.AuthRules;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Lehr
 * @create: 2020-03-28
 */
@Service
@Slf4j
public class AuthService {


    @Autowired
    private AuthRules authRules;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    private static AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 解析成功就返回，不然就是null
     *
     * @param token
     * @return
     */
    public JSONObject parseJwt(String token) {
        if (token == null) {
            return null;
        }
        try {
            //调用uaa模块检查合法性
            ServiceInstance serviceInstance = loadBalancerClient.choose("QuizHub-UAA");
            String path = String.format("http://%s:%s/oauth/check_token?token={token}", serviceInstance.getHost(), serviceInstance.getPort());
            String jsonToken = restTemplate.getForObject(path, String.class, token);
            return JSONObject.parseObject(jsonToken);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean hasAuth(String uri, String method, JSONObject json) {
        //检查是否登录
        String userId = "auth-fail";
        if (json != null) {
            userId = json.getString("userId");
        }

        List<AuthRules.AuthRule> rules = authRules.getRules();

        for (AuthRules.AuthRule rule : rules) {

            if (antPathMatcher.match(rule.antMatcher, uri)) {
                if (rule.authenticated) {

                    //如果只要求登录的情况
                    if (rule.authorities.isEmpty()) {
                        return (!"auth-fail".equals(userId));
                    }

                    //这个是权限检查，不一定需要登录了，而是看有没有权限
                    if (rule.authorities.contains(method)) {
                        //rpc check authorities
                        return checkAuthorities(uri, method, userId);
                    }
                }

                return true;
            }
        }
        //进行权限检查
        return true;
    }

    private Boolean checkAuthorities(String uri, String method, String userId) {
        try {
            //调用rbac模块检查权限
            ServiceInstance serviceInstance = loadBalancerClient.choose("QuizHub-RBAC");
            String path = String.format("http://%s:%s/%s?service={method}&visitorId={userId}",
                    serviceInstance.getHost(), serviceInstance.getPort(),
                    uri + "/authorities"
            );
            String hasAuthorities = restTemplate.getForObject(path, String.class, method, userId);
            return Boolean.valueOf(hasAuthorities);
        } catch (Exception e) {
            log.warn("rbac模块调用异常！");
            return false;
        }
    }

}

package com.imlehr.quizhub.gateway.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imlehr.quizhub.gateway.bean.AuthRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Lehr
 * @create: 2020-03-28
 */
@Service
public class AuthService {


    @Autowired
    private AuthRules authRules;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;


    /**
     * 解析成功就返回，不然就是null
     * @param token
     * @return
     */
    public JSONObject parseJwt(String token) {
        if(token==null)
        {
            return null;
        }
        try{
            //调用uaa模块检查合法性
            ServiceInstance serviceInstance = loadBalancerClient.choose("QuizHub-UAA");
            String path = String.format("http://%s:%s/oauth/check_token?token={token}", serviceInstance.getHost(), serviceInstance.getPort());
            String jsonToken = restTemplate.getForObject(path, String.class, token);
            return JSONObject.parseObject(jsonToken);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public boolean hasAuth(String uri,JSONObject json)
    {
        //检查是否登录
        Boolean isAuthenticated = json!=null;
        List<String> auths = new ArrayList<>(0);
        //获取权限
        if(json!=null)
        {
            JSONArray jsonArray = json.getJSONArray("authorities");
            String[] auts = new String[jsonArray.size()];
            jsonArray.toArray(auts);
            auths = Arrays.asList(auts);
        }


        //进行权限检查
        return authRules.isAuthorized(uri,auths,isAuthenticated);
    }




}

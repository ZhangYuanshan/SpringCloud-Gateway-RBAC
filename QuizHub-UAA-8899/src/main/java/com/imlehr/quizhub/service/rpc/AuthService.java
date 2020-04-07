package com.imlehr.quizhub.service.rpc;

import com.imlehr.quizhub.javabean.po.ResponseMsg;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lehr
 * @create: 2020-03-27
 */
@FeignClient(value="QuizHub-UAA")
public interface AuthService {

    @PostMapping("/oauth/token")
    Object passwordLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              @RequestParam("grant_type") String grant_type,
                              @RequestParam("client_id") String client_id,
                              @RequestParam("client_secret") String client_secret);



}

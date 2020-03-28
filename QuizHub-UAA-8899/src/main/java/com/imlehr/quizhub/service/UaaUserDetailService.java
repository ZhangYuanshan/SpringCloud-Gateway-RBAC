package com.imlehr.quizhub.service;

import com.imlehr.quizhub.javabean.po.UserPO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UaaUserDetailService implements UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserPO("Lehr","$2a$10$f/Wa6ESfZEGBvu/R3OjiveRJRaZRqC1ON5cAHSl2Ym/E5VQeB/aPy");
        //密码就是lerie bcr加密看  String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
    }



}

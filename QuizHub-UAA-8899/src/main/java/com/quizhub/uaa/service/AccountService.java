package com.quizhub.uaa.service;

import com.quizhub.common.javabean.ResponseMsg;
import com.quizhub.uaa.dao.UserMapper;
import com.quizhub.uaa.javabean.GitHubInfo;
import com.quizhub.uaa.javabean.bo.UserBO;
import com.quizhub.uaa.javabean.dto.UserDTO;
import com.quizhub.uaa.client.AuthService;
import com.quizhub.uaa.javabean.po.UserPO;
import com.quizhub.uaa.utils.GithubUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Lehr
 * @create: 2020-02-04
 */

@Service
public class AccountService {


    @Autowired
    private AuthService authService;


    private final static String grant_type = "password";

    private final static String client_id = "Lehr";

    private final static String client_secret = "Lehr's_Secret";


    @Autowired
    private UserMapper userMapper;


    public ResponseMsg login(String username, String password)
    {
        try
        {
            Object result = authService.passwordLogin(username, password, grant_type, client_id, client_secret);
            return ResponseMsg.ofSuccess(result);

        }
        catch (Exception e)
        {
            return ResponseMsg.ofFail("1000","登录失败");
        }
    }

    public ResponseMsg register(UserDTO user)
    {
        try{
            UserPO userPO = new UserPO(user);
            UserBO userBO = userMapper.createUser(userPO);
            return login(userBO.getUsername(), user.getPassword());
        }
        catch (Exception e)
        {
            return ResponseMsg.ofFail("1002","注册失败");
        }
    }


    /**
     * 目前github登录注册的用户不支持改密码.....
     * @param code
     * @return
     */
    public ResponseMsg GithubLogin(String code){

        GitHubInfo github = null;

        try{
            github = GithubUtils.getInfoFromGithub(code);
        }catch (Exception e)
        {
            return ResponseMsg.ofFail("1001","Github授权失败，请重试");
        }


        UserBO user = userMapper.getUserByGithubId(github.getUserId());

        //如果没有账号，就自动注册了登录
        if(user==null)
        {
            UserDTO newUser = new UserDTO(github);
            return register(newUser);
        }

        //如果有账号就直接登录
        return login(user.getUsername(), github.getLoginName());

    }





}

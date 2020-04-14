package com.quizhub.gitserver.service.impl;

import com.quizhub.common.javabean.ResponseMsg;
import com.quizhub.gitserver.client.AuthenticateClient;
import com.quizhub.gitserver.client.AuthorityClient;
import com.quizhub.gitserver.service.GitTransportService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.util.Base64;
import java.util.LinkedHashMap;

/**
 * @author Lehr
 * @create: 2020-04-01
 */
@Service
@Slf4j
public class GitTransportServiceImpl implements GitTransportService {


    @Autowired
    private AuthenticateClient authenticateClient;

    @Autowired
    private AuthorityClient authorityClient;


    private static final String gitRoot = "/home/" + System.getProperty("user.name") + "/GitRepo/";

    private Boolean hasAuthentication(HttpServletRequest req, HttpServletResponse res, String repo, String owner, String service) {

        Boolean hasAuth = false;

        try {

            String authorization = req.getHeader("Authorization");

            //获取认证信息 如果没有认证信息则直接失败
            if (authorization != null) {

                //减去前面basic字符串，并base64解密，获取到用户和密码
                byte[] decode = Base64.getDecoder().decode(authorization.substring(6));
                String[] auth = new String(decode).split(":");
                String username = auth[0];
                String password = auth[1];

                ResponseMsg responseMsg = authenticateClient.httpAuth(username, password);
                if (responseMsg.getSuccess()) {
                    //检查认证信息 目前repo就不检查拥有者了
                    LinkedHashMap data = (LinkedHashMap) responseMsg.getData();
                    String visitorId = data.get("userId").toString();
                    hasAuth = authorityClient.repoTransCheck(visitorId, owner, repo, service);
                }

            }


        } catch (Exception e) {
            hasAuth = false;
            log.warn("认证解析错误", e);
        }

        if (!hasAuth) {
            res.setStatus(401);
            res.setHeader("WWW-Authenticate", "Basic realm=\"Lehr's Quizhub\"");
        }

        return hasAuth;

    }

    @SneakyThrows
    @Override
    public void getRequest(String repo, String owner, String service, HttpServletRequest req, HttpServletResponse res) {


        //检查权限
        if (!hasAuthentication(req, res, repo, owner, service)) {
            return;
        }

        //获取仓库的绝对地址
        String path = gitRoot + owner + File.separator + repo;

        //第一次只用把他等待输入之前的消息，也就是advertise-refs，拿走就ok了
        Process process = Runtime.getRuntime().exec(service + " --advertise-refs " + path);

        //先准备好那个头信息 具体去看文档，这里的两种情况是有区别的
        String type = "e";
        if (service.contains("receive")) {
            type = "f";
        }

        //准备响应的第一行，这个是规范要求
        ServletOutputStream outputStream = res.getOutputStream();
        outputStream.write(("001" + type + "# service=" + service + "\n0000").getBytes());

        //把输出的refs信息放入响应体里
        process.getInputStream().transferTo(outputStream);



        //准备响应头
        res.setHeader("Content-Type", "application/x-" + service + "-advertisement");
        res.setHeader("Cache-Control", "no-cache");

    }


    @SneakyThrows
    @Override
    public void postRequest(byte[] info, String repo, String owner, String service, HttpServletRequest req, HttpServletResponse res) {

        //检查权限
        if (!hasAuthentication(req, res, repo, owner, service)) {
            return;
        }

        //获取仓库的绝对地址
        String path = gitRoot + owner + File.separator + repo;

        //第二次不需要advertise-ref，所以使用--stateless-rpc，输入内容，给出结果即可
        Process process = Runtime.getRuntime().exec(service + " --stateless-rpc " + path);

        //输入内容
        OutputStream outputStream = process.getOutputStream();
        outputStream.write(info);
        outputStream.flush();



        //把结果放入到响应输出里去
        process.getInputStream().transferTo(res.getOutputStream());


        //设置响应头，完成任务
        res.setHeader("Content-Type", "application/x-" + service + "-result");
        res.setHeader("Cache-Control", "no-cache");

    }


}

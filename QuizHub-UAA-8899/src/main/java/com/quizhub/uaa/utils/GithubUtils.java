package com.quizhub.uaa.utils;

import com.alibaba.fastjson.JSONObject;
import com.quizhub.uaa.javabean.GitHubInfo;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author Lehr
 * @create: 2020-03-10
 */
public class GithubUtils {

    //调试可以使用：
    // https://github.com/login/oauth/authorize?client_id=b79a86ede986fc56f408&redirect_uri=http://localhost:9999/accounts/githubAuth


    private final static String CLIENT_SECRET = "0bb7f75efb20cb48443ebb0a005bce5eebd9f68b";

    private final static String CLINENT_ID = "b79a86ede986fc56f408";

    private final static String TOKEN_URL = "https://github.com/login/oauth/access_token";

    private final static String INFO_URL = "https://api.github.com/user";

    private final static HttpClient client = HttpClientBuilder.create().build();

    /**
     * 返回一条完整的带有参数的token请求链接
     * @param code
     * @return
     */
    public static String getTokenUrl(String code)
    {
        return TOKEN_URL+"?client_id="+CLINENT_ID+"&client_secret="+CLIENT_SECRET+"&code="+code;
    }

    public static String getInfoUrl(String token)
    {
        return INFO_URL+"?access_token="+token;
    }


    @SneakyThrows
    public static GitHubInfo getInfoFromGithub(String code) {



        HttpPost request = new HttpPost(GithubUtils.getTokenUrl(code));

        HttpResponse response = client.execute(request);


        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        String responseStr = rd.readLine();

        String accessToken = responseStr.substring(responseStr.indexOf('=')+1,responseStr.indexOf('&'));

        //添加请求头
        HttpGet request2 = new HttpGet(GithubUtils.getInfoUrl(accessToken));

        HttpResponse response2 = client.execute(request2);

        BufferedReader rd2 = new BufferedReader(
                new InputStreamReader(response2.getEntity().getContent()));

        String line = "";

        StringBuilder result = new StringBuilder();
        while ((line = rd2.readLine()) != null) {
            result.append(line);
        }

        String infos = result.toString();
        JSONObject info = JSONObject.parseObject(infos);
        return new GitHubInfo(info);

    }


}

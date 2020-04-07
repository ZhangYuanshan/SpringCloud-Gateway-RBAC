package com.imlehr.quizhub.javabean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author Lehr
 * @create: 2020-03-30
 */
@Data
public class GitHubInfo {

    private String loginName;
    private String userId;
    private String nodeId;
    private String avatarUrl;
    private String bio;
    private String username;
    private String htmlUrl;



    public GitHubInfo(JSONObject info)
    {
        loginName = info.getString("login");
        userId = info.getString("id");
        nodeId = info.getString("node_id");
        avatarUrl = info.getString("avatar_url");
        bio = info.getString("bio");
        username = info.getString("name");
        htmlUrl = info.getString("html_url");
    }
}

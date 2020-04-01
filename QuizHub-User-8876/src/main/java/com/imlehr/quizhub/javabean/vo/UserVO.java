package com.imlehr.quizhub.javabean.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lehr
 * @create: 2020-04-02
 * 一个用于传递给前端展示的实体类JavaBean
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserVO {

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户个人简介
     */
    private String userBio;

    /**
     * 用户头像url
     */
    private String avatarUrl;

    /**
     * 用户个人网站地址
     */
    private String htmlUrl;

    /**
     * 模型转换的构造方法,po到vo
     * @param po
     */
    public UserVO(UserPO po)
    {
        userId = po.getUserId();
        userBio = po.getUserBio();
        username = po.getUsername();
        //这里可以做一些判断操作比如url是空你就补充一个默认的
        avatarUrl = po.getAvatarUrl();
        htmlUrl = po.getHtmlUrl();
    }

}

package com.imlehr.quizhub.javabean.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Lehr
 * @create: 2020-04-02
 * 一个用于表示数据库查询结果的实体类
 * 由于有可能会涉及到redis传输序列化，所以需要实现Serializable接口
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserPO implements Serializable {

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
     * github绑定的id
     */
    private String githubId;

    /**
     * 一些其他的实际上前端不需要用到的属性
     */
    private String nothing;

}

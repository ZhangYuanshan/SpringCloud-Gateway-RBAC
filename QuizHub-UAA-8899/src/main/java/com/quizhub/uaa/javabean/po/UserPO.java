package com.quizhub.uaa.javabean.po;

import com.quizhub.uaa.javabean.dto.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * @author Lehr
 * @create: 2020-03-27
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserPO {

    private String userId;

    private String username;

    private String password;

    private String githubId;

    private String avatarUrl;

    private String bio;

    private String htmlUrl;

    public UserPO(UserDTO user)
    {
        //github默认的密码就是用户登录名加密
        this.password = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        this.username = user.getUsername();

        this.githubId = user.getGithubId();

        this.avatarUrl = user.getAvatarUrl();

        this.bio = user.getBio();

        this.htmlUrl = user.getHtmlUrl();
    }

}

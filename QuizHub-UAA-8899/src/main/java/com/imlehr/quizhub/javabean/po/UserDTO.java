package com.imlehr.quizhub.javabean.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Lehr
 * @create: 2020-03-27
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserDTO {

    private String username;

    private String password;

    private String githubId;

    private String avatarUrl;

    private String bio;

    private String htmlUrl;

    public UserDTO(GitHubInfo github)
    {
        this.password = github.getLoginName();
        this.username = github.getUsername();
        this.githubId = github.getUserId();
        this.avatarUrl = github.getAvatarUrl();
        this.bio = github.getBio();
        this.htmlUrl = github.getHtmlUrl();
    }

}

package com.quizhub.uaa.javabean.dto;

import com.quizhub.uaa.javabean.GitHubInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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

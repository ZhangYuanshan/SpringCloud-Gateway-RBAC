package com.imlehr.quizhub.javabean.po;

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
public class RepoAuthPO {

    private String repoId;

    private String repoOwner;

    private String repoName;

    private String visitorId;

    private String service;



}

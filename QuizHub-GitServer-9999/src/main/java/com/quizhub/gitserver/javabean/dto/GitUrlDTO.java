package com.quizhub.gitserver.javabean.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Lehr
 * @create: 2020-04-11
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class GitUrlDTO{


    private String httpUrl;
    private String sshUrl;

}

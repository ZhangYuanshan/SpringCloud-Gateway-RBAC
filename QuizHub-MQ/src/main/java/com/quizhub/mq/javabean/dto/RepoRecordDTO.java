package com.quizhub.mq.javabean.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Lehr
 * @create: 2020-04-12
 */
@Data
@Accessors(chain = true)
public class RepoRecordDTO {

    private String userId;
    private String repoTagId;
    private String owner;
    private String repoName;

}

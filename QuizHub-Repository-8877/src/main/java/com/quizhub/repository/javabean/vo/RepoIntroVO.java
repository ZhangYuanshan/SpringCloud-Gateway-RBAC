package com.quizhub.repository.javabean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Lehr
 * @create: 2020-04-11
 */
@Data
@ApiModel("仓库概览")
@Accessors(chain = true)
public class RepoIntroVO {

    @ApiModelProperty(value = "仓库名称")
    private String repoName;

    @ApiModelProperty(value = "仓库简介")
    private String repoIntro;

    @ApiModelProperty(value = "仓库标签")
    private String tagName;

    @ApiModelProperty(value = "是否公开")
    private Boolean isPublic;

    @ApiModelProperty(value = "fork数目")
    private Integer forkNum;

    @ApiModelProperty(value = "star数目")
    private Integer starNum;

    @ApiModelProperty(value = "watch数目")
    private Integer watchNum;

}

package com.quizhub.repository.javabean.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Lehr
 * @create: 2020-04-11
 */
@ApiModel("文件概述")
@Data
@Accessors(chain = true)
public class FileInfo {

    @ApiModelProperty(value = "文件名称")
    private String filename;

    @ApiModelProperty(value = "文件在git仓库中的路径")
    private String gitPath;

    @ApiModelProperty(value = "提交日期")
    private String commitDate;

    @ApiModelProperty(value = "提交信息")
    private String commitMessage;

    @ApiModelProperty(value = "是否是目录")
    private Boolean isDir;

}

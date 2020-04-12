package com.quizhub.repository.javabean.po;

import com.quizhub.repository.javabean.entity.FileInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Lehr
 * @create: 2020-04-11
 */
@Data
@ApiModel("仓库详细")
@Accessors(chain = true)
public class RepoPO {

    @ApiModelProperty(value = "仓库名称")
    private String repoName;

    @ApiModelProperty(value = "仓库拥有者的昵称")
    private String username;

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

    @ApiModelProperty(value = "git http交互链接")
    private String httpUri;

    @ApiModelProperty(value = "仓库文件列表")
    private List<FileInfo> fileInfoList;

    @ApiModelProperty(value = "readme的内容")
    private byte[] readmeContent;

}

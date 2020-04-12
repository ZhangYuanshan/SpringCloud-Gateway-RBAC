package com.quizhub.gitserver.javabean.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Lehr
 * @create: 2020-04-12
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class GitOverviewDTO {

    private List<GitFileInfoDTO> files;

    private byte[] readme;


}

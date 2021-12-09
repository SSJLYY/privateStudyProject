package com.dcy.file.dto.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/11/12 11:18
 */
@Getter
@Setter
@ApiModel(value = "FileInfoListOutputDTO", description = "文件信息")
public class FileInfoListOutputDTO {

    @ApiModelProperty(value = "文件id")
    private String id;

    @ApiModelProperty(value = "文件名称")
    private String name;

    @ApiModelProperty(value = "文件类型")
    private String contentType;

    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    @ApiModelProperty(value = "物理路径")
    private String path;

    @ApiModelProperty(value = "url地址")
    private String url;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

}

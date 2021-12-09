package com.dcy.file.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @Author：dcy
 * @Description: 下载文件dto
 * @Date: 2020/10/22 13:52
 */
@Getter
@Setter
@ApiModel(value = "FileInfoDownloadInputDTO", description = "下载文件DTO")
public class FileInfoDownloadInputDTO {

    @ApiModelProperty(value = "文件名称")
    @NotBlank(message = "文件名称不能为空")
    private String fileName;

    @ApiModelProperty(value = "文件路径")
    @NotBlank(message = "文件路径不能为空")
    private String filePath;
}

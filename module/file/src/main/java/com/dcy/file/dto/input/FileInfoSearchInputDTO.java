package com.dcy.file.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/11/12 11:18
 */
@Getter
@Setter
@ApiModel(value = "FileInfoSearchInputDTO", description = "文件查询参数")
public class FileInfoSearchInputDTO {

    @ApiModelProperty(value = "文件名称")
    private String name;

}

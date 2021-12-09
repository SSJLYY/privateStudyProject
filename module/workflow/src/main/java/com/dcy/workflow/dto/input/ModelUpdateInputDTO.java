package com.dcy.workflow.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/6/7 11:12
 */
@Getter
@Setter
@ApiModel(value = "ModelUpdateInputDTO", description = "修改模型表单")
public class ModelUpdateInputDTO {

    @ApiModelProperty(value = "模型id")
    @NotBlank(message = "模型id不能为空")
    private String id;

    @ApiModelProperty(value = "业务key")
    @NotBlank(message = "业务key不能为空")
    private String key;

    @ApiModelProperty(value = "类别")
    private String category;

    @ApiModelProperty(value = "流程模型名称")
    @NotBlank(message = "流程模型名称不能为空")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "流程文件")
    @NotBlank(message = "流程文件不能为空")
    private String jsonXml;

    @ApiModelProperty(value = "svg图片")
    @NotBlank(message = "svg图片名称不能为空")
    private String svgXml;
}

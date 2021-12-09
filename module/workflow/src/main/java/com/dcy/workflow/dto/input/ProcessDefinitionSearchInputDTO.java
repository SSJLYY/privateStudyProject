package com.dcy.workflow.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/6/7 10:01
 */
@Getter
@Setter
@ApiModel(value = "ProcessDefinitionSearchInputDTO", description = "流程定义查询")
public class ProcessDefinitionSearchInputDTO {

    @ApiModelProperty(value = "流程定义名称")
    private String name;

    @ApiModelProperty(value = "业务名称")
    private String key;

}

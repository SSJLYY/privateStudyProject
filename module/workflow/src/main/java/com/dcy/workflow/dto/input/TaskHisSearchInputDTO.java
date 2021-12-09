package com.dcy.workflow.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/6/7 13:08
 */
@Getter
@Setter
@ApiModel(value = "TaskHisSearchInputDTO", description = "已办任务查询表单")
public class TaskHisSearchInputDTO {

    @ApiModelProperty(value = "流程定义名称")
    private String processDefinitionName;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

}

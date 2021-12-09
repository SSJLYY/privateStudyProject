package com.dcy.workflow.dto.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/6/11 15:50
 */
@ToString
@Getter
@Setter
@Accessors(chain = true)
@ApiModel(value = "HisTaskListOutputDTO", description = "已办任务列表")
public class HisTaskListOutputDTO {

    @ApiModelProperty(value = "任务id")
    private String taskId;

    @ApiModelProperty(value = "流程实例id")
    private String processInstanceId;

    @ApiModelProperty(value = "流程实例名称")
    private String processInstanceName;

    @ApiModelProperty(value = "流程定义名称")
    private String processDefinitionName;

    @ApiModelProperty(value = "业务标识")
    private String businessKey;

    @ApiModelProperty(value = "业务表标识")
    private String tableName;

    @ApiModelProperty(value = "业务表id")
    private String formId;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "发起人id")
    private String startUserId;

    @ApiModelProperty(value = "发起人名称")
    private String startUserName;

    @ApiModelProperty(value = "发起时间")
    private Date startTime;

    @ApiModelProperty(value = "办理时间")
    private Date endTime;

    @ApiModelProperty(value = "任务历时")
    private String duration;
}

package com.dcy.workflow.dto.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;

import java.util.Date;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/6/7 10:01
 */
@ToString
@Getter
@Setter
@ApiModel(value = "ProcessInstanceListOutputDTO", description = "流程运行结果")
public class ProcessInstanceListOutputDTO {

    @ApiModelProperty(value = "流程实例id")
    private String processInstanceId;

    @ApiModelProperty(value = "流程定义id")
    private String processDefinitionId;

    @ApiModelProperty(value = "流程定义名称")
    private String processDefinitionName;

    @ApiModelProperty(value = "流程定义业务key")
    private String processDefinitionKey;

    @ApiModelProperty(value = "流程定义版本")
    private String processDefinitionVersion;

    @ApiModelProperty(value = "业务key")
    private String businessKey;

    @ApiModelProperty(value = "部署id")
    private String deploymentId;

    @ApiModelProperty(value = "流程实例状态 1 激活 2 挂起")
    private Boolean suspendState;

    @ApiModelProperty(value = "运行实例名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "启动时间")
    private Date startTime;

    @ApiModelProperty(value = "启动人")
    private String startUserId;


    public ProcessInstanceListOutputDTO(ProcessInstance processInstance) {
        this.processInstanceId = processInstance.getId();
        this.processDefinitionId = processInstance.getProcessDefinitionId();
        this.processDefinitionName = processInstance.getProcessDefinitionName();
        this.processDefinitionKey = processInstance.getProcessDefinitionKey();
        this.deploymentId = processInstance.getDeploymentId();
        this.businessKey = processInstance.getBusinessKey();
        this.suspendState = processInstance.isSuspended();
        this.name = processInstance.getName();
        this.description = processInstance.getDescription();
        this.startTime = processInstance.getStartTime();
        this.startUserId = processInstance.getStartUserId();
    }
}

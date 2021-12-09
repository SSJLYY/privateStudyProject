package com.dcy.workflow.dto.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;

import java.util.Date;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/6/7 10:01
 */
@Getter
@Setter
@ApiModel(value = "ProcessDefinitionListOutputDTO", description = "流程定义结果")
public class ProcessDefinitionListOutputDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "流程分类")
    private String category;

    @ApiModelProperty(value = "流程定义名称")
    private String name;

    @ApiModelProperty(value = "业务key")
    private String key;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "版本")
    private Integer version;

    @ApiModelProperty(value = "部署id")
    private String deploymentId;

    @ApiModelProperty(value = "资源名称")
    private String resourceName;

    @ApiModelProperty(value = "部署时间")
    private Date deploymentTime;

    @ApiModelProperty(value = "流程实例状态 false 激活 ; true 挂起")
    private Boolean suspendState;

    @ApiModelProperty(value = "资源名称")
    private String diagramResourceName;

    @ApiModelProperty(value = "部署名称")
    private String deploymentName;

    @ApiModelProperty(value = "部署类型")
    private String deploymentCategory;

    @ApiModelProperty(value = "部署key")
    private String deploymentKey;

    public ProcessDefinitionListOutputDTO() {

    }

    public ProcessDefinitionListOutputDTO(ProcessDefinition processDefinition, Deployment deployment) {
        this.id = processDefinition.getId();
        this.category = processDefinition.getCategory();
        this.name = processDefinition.getName();
        this.key = processDefinition.getKey();
        this.description = processDefinition.getDescription();
        this.version = processDefinition.getVersion();
        this.resourceName = processDefinition.getResourceName();
        this.deploymentId = processDefinition.getDeploymentId();
        this.diagramResourceName = processDefinition.getDiagramResourceName();
        this.suspendState = processDefinition.isSuspended();
        this.deploymentName = deployment.getName();
        this.deploymentTime = deployment.getDeploymentTime();
        this.deploymentCategory = deployment.getCategory();
        this.deploymentKey = deployment.getKey();
    }
}

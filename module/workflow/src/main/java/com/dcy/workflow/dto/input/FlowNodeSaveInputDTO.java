package com.dcy.workflow.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 流程节点表
 * </p>
 *
 * @author dcy
 * @since 2021-06-08
 */
@Getter
@Setter
@ApiModel(value="FlowNodeSaveInputDTO", description="流程节点保存表单")
public class FlowNodeSaveInputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "流程定义id")
    private String procDefId;

    @ApiModelProperty(value = "流程节点id")
    private String flowId;

    @ApiModelProperty(value = "流程节点名称")
    private String flowName;

    @ApiModelProperty(value = "流程审批类型（1：用户；2：角色；3：部门）")
    private String flowType;

    @ApiModelProperty(value = "类型的ids（用户ids or 角色ids or 部门ids）")
    private List<String> ids;
}

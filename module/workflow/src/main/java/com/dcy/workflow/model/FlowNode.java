package com.dcy.workflow.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

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
@Data
@Accessors(chain = true)
@TableName("act_flow_node")
public class FlowNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 流程定义id
     */
    private String procDefId;

    /**
     * 流程节点id
     */
    private String flowId;

    /**
     * 流程节点名称
     */
    private String flowName;

    /**
     * 流程审批类型（1：用户；2：角色；3：部门）
     */
    private String flowType;

    /**
     * 审批ids
     */
    @TableField(exist = false)
    private List<String> ids;


    public static final String ID = "id";

    public static final String PROC_DEF_ID = "proc_def_id";

    public static final String FLOW_ID = "flow_id";

    public static final String FLOW_NAME = "flow_name";

    public static final String FLOW_TYPE = "flow_type";

}

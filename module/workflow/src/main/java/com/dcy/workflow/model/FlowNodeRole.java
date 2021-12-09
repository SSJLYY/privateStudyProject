package com.dcy.workflow.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 任务节点关联角色表
 * </p>
 *
 * @author dcy
 * @since 2021-06-08
 */
@Data
@Accessors(chain = true)
@TableName("act_flow_node_role")
public class FlowNodeRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程节点id
     */
    private String flowNodeId;

    /**
     * 角色id
     */
    private String roleId;


    public static final String FLOW_NODE_ID = "flow_node_id";

    public static final String ROLE_ID = "role_id";

}

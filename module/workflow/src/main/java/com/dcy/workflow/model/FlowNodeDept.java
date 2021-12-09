package com.dcy.workflow.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dcy.db.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 任务节点关联部门表
 * </p>
 *
 * @author dcy
 * @since 2021-06-08
 */
@Data
@Accessors(chain = true)
@TableName("act_flow_node_dept")
public class FlowNodeDept implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程节点id
     */
    private String flowNodeId;

    /**
     * 部门id
     */
    private String deptId;


    public static final String FLOW_NODE_ID = "flow_node_id";

    public static final String DEPA_ID = "depa_id";

}

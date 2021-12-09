package com.dcy.workflow.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dcy.db.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 任务节点关联用户表
 * </p>
 *
 * @author dcy
 * @since 2021-06-08
 */
@Data
@Accessors(chain = true)
@TableName("act_flow_node_user")
public class FlowNodeUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程节点id
     */
    private String flowNodeId;

    /**
     * 用户id
     */
    private String userId;


    public static final String FLOW_NODE_ID = "flow_node_id";

    public static final String USER_ID = "user_id";

}

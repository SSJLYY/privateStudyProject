package com.dcy.common.model.flowable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/6/7 10:01
 */
@ToString
@Getter
@Setter
@Accessors(chain = true)
public class ActProcessInstance {

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 流程定义id
     */
    private String processDefinitionId;

    /**
     * 流程定义名称
     */
    private String processDefinitionName;

    /**
     * 流程定义业务key
     */
    private String processDefinitionKey;


    /**
     * 业务key
     */
    private String businessKey;

    /**
     * 运行实例名称
     */
    private String name;

    /**
     * 启动时间
     */
    private Date startTime;

    /**
     * 启动人
     */
    private String startUserId;

}

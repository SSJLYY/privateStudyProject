package com.dcy.common.service;

import com.dcy.common.model.flowable.ActProcessInstance;

import java.util.Map;

/**
 * @Author：dcy
 * @Description: 工作流service
 * @Date: 2021/6/11 14:16
 */
public interface ActFlowableTaskService {


    /**
     * 启动流程
     *
     * @param processDefinitionKey 流程定义key
     * @param businessKey          业务key（格式：表名:主键id）
     * @param userId               用户id
     * @return
     */
    ActProcessInstance startProcess(String processDefinitionKey, String businessKey, String userId);

    /**
     * 启动任务并且完成首个任务
     *
     * @param processDefinitionKey 流程定义key
     * @param businessKey          业务key（格式：表名:主键id）
     * @param userId               用户id
     * @return
     */
    ActProcessInstance startProcessAndCompleteFirstTask(String processDefinitionKey, String businessKey, String userId);


    /**
     * 完成任务
     *
     * @param taskId 任务id
     * @param userId 用户id
     */
    void complete(String taskId, String userId);


    /**
     * 完成任务
     *
     * @param taskId  任务id
     * @param userId  用户id
     * @param comment 审批批注
     */
    void complete(String taskId, String userId, String comment);


    /**
     * 完成任务
     *
     * @param taskId    任务id
     * @param userId    用户id
     * @param comment   审批批注
     * @param variables 流程变量
     */
    void complete(String taskId, String userId, String comment, Map<String, Object> variables);

    /**
     * 完成任务
     *
     * @param taskId  任务id
     * @param userId  用户id
     * @param comment 审批批注
     * @param adopt   审核状态（success;reject）
     */
    void complete(String taskId, String userId, String comment, Boolean adopt);


    /**
     * 完成任务并且设置流程变量
     *
     * @param taskId
     * @param userId
     * @param comment
     * @param adopt
     */
    void completeAndSetVar(String taskId, String userId, String comment, Boolean adopt);

    /**
     * 完成任务
     *
     * @param taskId    任务id
     * @param userId    用户id
     * @param comment   审批批注
     * @param adopt     审核状态（success;reject）
     * @param variables 流程变量
     */
    void complete(String taskId, String userId, String comment, Boolean adopt, Map<String, Object> variables);

    /**
     * 删除流程实例
     *
     * @param processInstanceId 流程id
     */
    void deleteProcess(String processInstanceId);

    /**
     * 根据流程实例id获取任务id
     *
     * @param processInstanceId 流程id
     * @param userId            用户id
     * @return
     */
    String getTaskIdByProcessInstanceId(String processInstanceId, String userId);
}

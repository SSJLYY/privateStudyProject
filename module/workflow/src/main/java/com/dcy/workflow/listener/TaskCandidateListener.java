package com.dcy.workflow.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dcy.common.enums.FlowTypeEnum;
import com.dcy.workflow.model.FlowNode;
import com.dcy.workflow.service.FlowNodeService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngines;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.runtime.Execution;
import org.flowable.task.service.delegate.DelegateTask;

import java.util.List;

/**
 * @Author：dcy
 * @Description: 设置用户任务审批人监听器
 * @Date: 2021/6/8 8:28
 * Assignee: 任务的受理人，即执行人。它有两种情况（有值,NULL)
 * Owner: 任务的委托人。
 * CandidateGroup: 候选用户组
 * CandidateUser: 候选人
 * delegateTask: 委派任务/签收的任务
 * resolveTask: 委派任务的代办，任务的拥有者把任务委派他人来办理，他人办完后，又重新回到任务拥有者，会产生流转记录。
 * turnTask： 转办任务，只是改变当前任务的办理人而已，不会产生流转记录。
 * CompleteTask: 完成任务，或叫办结提交下一步。
 * claimTask：任务签收
 */
@Slf4j
public class TaskCandidateListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // ====================获取基本信息========================
        final String processDefinitionId = delegateTask.getProcessDefinitionId();
        final Execution execution = processEngine.getRuntimeService().createExecutionQuery().executionId(delegateTask.getExecutionId()).singleResult();

        log.info("TaskCandidateListener activityId：{}", execution.getActivityId());
        log.info("TaskCandidateListener 流程定义id：{}", processDefinitionId);

        // ====================设置候选人 or 候选组========================
        final FlowNodeService flowNodeService = SpringUtil.getBean(FlowNodeService.class);
        final FlowNode flowNode = flowNodeService.getOne(Wrappers.<FlowNode>lambdaQuery()
                .eq(FlowNode::getProcDefId, processDefinitionId)
                .eq(FlowNode::getFlowId, execution.getActivityId())
                .last("LIMIT 1"));
        final FlowTypeEnum flowType = FlowTypeEnum.getByCode(flowNode.getFlowType());
        if (flowType != null) {
            switch (flowType) {
                case USER:
                    final List<String> ids = flowNodeService.getIds(flowNode);
                    if (CollUtil.isNotEmpty(ids) && CollUtil.size(ids) == 1) {
                        // 设置 受理人
                        delegateTask.setAssignee(CollUtil.getFirst(ids));
                    } else {
                        // 设置 候选人
                        delegateTask.addCandidateUsers(ids);
                    }
                    break;
                case ROLE:
                case DEPT:
                    // 设置 候选组
                    delegateTask.addCandidateGroups(flowNodeService.getIds(flowNode));
                    break;
                default:
            }
        }


    }

}

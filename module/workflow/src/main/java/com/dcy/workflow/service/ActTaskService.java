package com.dcy.workflow.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.StrUtil;
import com.dcy.common.model.PageModel;
import com.dcy.common.model.flowable.ActProcessInstance;
import com.dcy.common.service.ActFlowableTaskService;
import com.dcy.common.service.ActUserService;
import com.dcy.workflow.dto.input.TaskRunSearchInputDTO;
import com.dcy.workflow.dto.output.ActivityInstanceListOutputDTO;
import com.dcy.workflow.dto.output.HisTaskListOutputDTO;
import com.dcy.workflow.dto.output.TaskListOutputDTO;
import com.google.common.collect.ImmutableMap;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dcy.common.constant.TaskConstants.*;


/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/6/7 10:48
 */
@Service
public class ActTaskService implements ActFlowableTaskService {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ActUserService actUserService;
    @Autowired
    private RepositoryService repositoryService;

    /**
     * 启动流程
     *
     * @param processDefinitionKey 流程定义key
     * @param businessKey          业务key（格式：表名:主键id）
     * @param userId               用户id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ActProcessInstance startProcess(String processDefinitionKey, String businessKey, String userId) {
        // 设置发起人
        identityService.setAuthenticatedUserId(userId);
        final ImmutableMap<String, Object> variables = ImmutableMap.of(TASK_BUSINESS_KEY, businessKey, "userId", userId);
        // 根据流程 key 启动流程
        final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        // 流程实例设置名称
        final String nickName = actUserService.getNickNameByUserId(userId);
        runtimeService.setProcessInstanceName(processInstance.getId(),
                StrUtil.format("{}在{}发起了{}", nickName, DateUtil.formatDateTime(processInstance.getStartTime()), processInstance.getProcessDefinitionName()));
        return new ActProcessInstance()
                .setProcessInstanceId(processInstance.getId())
                .setProcessDefinitionId(processInstance.getId())
                .setProcessDefinitionId(processInstance.getProcessDefinitionId())
                .setProcessDefinitionName(processInstance.getProcessDefinitionName())
                .setProcessDefinitionKey(processInstance.getProcessDefinitionKey())
                .setBusinessKey(processInstance.getBusinessKey())
                .setName(processInstance.getName())
                .setStartTime(processInstance.getStartTime())
                .setStartUserId(processInstance.getStartUserId());
    }

    /**
     * 启动任务并且完成首个任务
     *
     * @param processDefinitionKey 流程定义key
     * @param businessKey          业务key（格式：表名:主键id）
     * @param userId               用户id
     * @return
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
    @Override
    public ActProcessInstance startProcessAndCompleteFirstTask(String processDefinitionKey, String businessKey, String userId) {
        // 设置发起人
        identityService.setAuthenticatedUserId(userId);
        final ImmutableMap<String, Object> variables = ImmutableMap.of(TASK_BUSINESS_KEY, businessKey, "userId", userId);
        // 根据流程 key 启动流程
        final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        // 流程实例设置名称
        final String nickName = actUserService.getNickNameByUserId(userId);
        runtimeService.setProcessInstanceName(processInstance.getId(),
                StrUtil.format("{}在{}发起了{}", nickName, DateUtil.formatDateTime(processInstance.getStartTime()), processInstance.getProcessDefinitionName()));
        final Task task = taskService.createTaskQuery()
                .processDefinitionId(processInstance.getProcessDefinitionId())
                .processDefinitionKey(processInstance.getProcessDefinitionKey())
                .processInstanceId(processInstance.getProcessInstanceId())
                .processInstanceBusinessKey(businessKey)
                .taskAssignee(userId)
                .singleResult();
        // 完成第一个任务
        taskService.complete(task.getId());
        return new ActProcessInstance()
                .setProcessInstanceId(processInstance.getId())
                .setProcessDefinitionId(processInstance.getId())
                .setProcessDefinitionId(processInstance.getProcessDefinitionId())
                .setProcessDefinitionName(processInstance.getProcessDefinitionName())
                .setProcessDefinitionKey(processInstance.getProcessDefinitionKey())
                .setBusinessKey(processInstance.getBusinessKey())
                .setName(processInstance.getName())
                .setStartTime(processInstance.getStartTime())
                .setStartUserId(processInstance.getStartUserId());
    }

    /**
     * 签收任务
     *
     * @param taskId
     * @param userId
     */
    public void claim(String taskId, String userId) {
        taskService.claim(taskId, userId);
    }


    /**
     * 完成任务
     *
     * @param taskId
     * @param userId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void complete(String taskId, String userId) {
        Task task = getTaskByTaskIdAndUserId(taskId, userId);
        if (task != null && StrUtil.isNotBlank(userId)) {
            // 签收任务
            taskService.claim(task.getId(), userId);
            // 设置完成人
            taskService.setAssignee(task.getId(), userId);
        }
        taskService.complete(taskId);
    }

    /**
     * 完成任务
     *
     * @param taskId
     * @param userId
     * @param comment
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void complete(String taskId, String userId, String comment) {
        Task task = getTaskByTaskIdAndUserId(taskId, userId);
        if (task != null && StrUtil.isNotBlank(userId)) {
            // 签收任务
            taskService.claim(task.getId(), userId);
            // 设置完成人
            taskService.setAssignee(task.getId(), userId);
        }
        if (task != null && StrUtil.isNotBlank(task.getProcessInstanceId()) && StrUtil.isNotBlank(comment)) {
            // 保存意见
            taskService.addComment(task.getId(), task.getProcessInstanceId(), TASK_STATUS, TASK_SUCCESS);
            taskService.addComment(task.getId(), task.getProcessInstanceId(), TASK_COMMENT, comment);
        }
        taskService.complete(taskId);
    }

    /**
     * 完成任务
     *
     * @param taskId
     * @param userId
     * @param comment
     * @param variables
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void complete(String taskId, String userId, String comment, Map<String, Object> variables) {
        Task task = getTaskByTaskIdAndUserId(taskId, userId);
        if (task != null && StrUtil.isNotBlank(userId)) {
            // 签收任务
            taskService.claim(task.getId(), userId);
            // 设置完成人
            taskService.setAssignee(task.getId(), userId);
        }
        if (task != null && StrUtil.isNotBlank(task.getProcessInstanceId()) && StrUtil.isNotBlank(comment)) {
            // 保存意见
            taskService.addComment(task.getId(), task.getProcessInstanceId(), TASK_STATUS, TASK_SUCCESS);
            taskService.addComment(task.getId(), task.getProcessInstanceId(), TASK_COMMENT, comment);
        }
        if (CollUtil.isNotEmpty(variables)) {
            taskService.complete(taskId, variables);
        } else {
            taskService.complete(taskId);
        }

    }

    /**
     * 完成任务
     *
     * @param taskId
     * @param userId
     * @param comment
     * @param adopt
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void complete(String taskId, String userId, String comment, Boolean adopt) {
        Task task = getTaskByTaskIdAndUserId(taskId, userId);
        if (task != null && StrUtil.isNotBlank(userId)) {
            // 签收任务
            taskService.claim(task.getId(), userId);
            // 设置完成人
            taskService.setAssignee(task.getId(), userId);
        }
        if (task != null && StrUtil.isNotBlank(task.getProcessInstanceId()) && StrUtil.isNotBlank(comment)) {
            // 保存意见
            taskService.addComment(task.getId(), task.getProcessInstanceId(), TASK_STATUS, adopt ? TASK_SUCCESS : TASK_REJECT);
            taskService.addComment(task.getId(), task.getProcessInstanceId(), TASK_COMMENT, comment);
        }
        taskService.complete(taskId);
    }

    /**
     * 完成任务并且设置流程变量
     *
     * @param taskId
     * @param userId
     * @param comment
     * @param adopt
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void completeAndSetVar(String taskId, String userId, String comment, Boolean adopt) {
        complete(taskId, userId, comment, adopt, ImmutableMap.of(TASK_STATUS, adopt ? TASK_SUCCESS : TASK_REJECT));
    }

    /**
     * 完成任务
     *
     * @param taskId
     * @param userId
     * @param comment
     * @param adopt
     * @param variables
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void complete(String taskId, String userId, String comment, Boolean adopt, Map<String, Object> variables) {
        Task task = getTaskByTaskIdAndUserId(taskId, userId);
        if (task != null && StrUtil.isNotBlank(userId)) {
            // 签收任务
            taskService.claim(task.getId(), userId);
            // 设置完成人
            taskService.setAssignee(task.getId(), userId);
        }
        if (task != null && StrUtil.isNotBlank(task.getProcessInstanceId()) && StrUtil.isNotBlank(comment)) {
            // 保存意见
            taskService.addComment(task.getId(), task.getProcessInstanceId(), TASK_STATUS, adopt ? TASK_SUCCESS : TASK_REJECT);
            taskService.addComment(task.getId(), task.getProcessInstanceId(), TASK_COMMENT, comment);
        }
        if (CollUtil.isNotEmpty(variables)) {
            taskService.complete(taskId, variables);
        } else {
            taskService.complete(taskId);
        }
    }

    /**
     * 查询任务
     *
     * @param taskId
     * @param userId
     * @return
     */
    private Task getTaskByTaskIdAndUserId(String taskId, String userId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task.isSuspended()) {
            return null;
        }
        if (StrUtil.isBlank(task.getAssignee()) && StrUtil.isBlank(userId)) {
            return null;
        }
        return task;
    }

    /**
     * 根据用户获取代表列表流程实例ids
     *
     * @param userId
     * @return
     */
    public List<String> getRunProInsIdList(String userId, Collection<String> groupIds) {
        // =============== 已签收和未签收同时查询 ===============
        TaskQuery taskQuery = taskService.createTaskQuery();
        if (StrUtil.isNotBlank(userId)) {
            taskQuery.taskCandidateOrAssigned(userId);
        }
        if (CollUtil.isNotEmpty(groupIds)) {
            taskQuery.taskCandidateGroupIn(groupIds);
        }
        return taskQuery.active().list().stream().map(TaskInfo::getProcessInstanceId).distinct().collect(Collectors.toList());
    }

    /**
     * 根据用户Id获取历史任务流程实例ids
     *
     * @param userId
     * @return
     */
    public List<String> getHisProInsIdList(String userId) {
        return historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userId).finished()
                .list().stream().map(TaskInfo::getProcessInstanceId).distinct().collect(Collectors.toList());
    }

    /**
     * 根据流程实例id删除流程实例
     *
     * @param processInstanceId
     */
    @Override
    public void deleteProcess(String processInstanceId) {
        final long count = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).count();
        if (count > 0) {
            runtimeService.deleteProcessInstance(processInstanceId, "删除流程");
        }
        final long hisCount = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).count();
        if (hisCount > 0) {
            historyService.deleteHistoricProcessInstance(processInstanceId);
        }
    }

    @Override
    public String getTaskIdByProcessInstanceId(String processInstanceId, String userId) {
        final Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskAssignee(userId)
                .active()
                .singleResult();
        if (task != null) {
            return task.getId();
        }
        return null;
    }


    /**
     * 获取任务节点信息+批注信息（如果运行是空，就去查询历史的）
     *
     * @param processInstanceId
     * @return
     */
    public List<ActivityInstanceListOutputDTO> getTaskInsList(String processInstanceId) {
        List<ActivityInstanceListOutputDTO> activityInstanceListOutputDTOS = new ArrayList<>();
        final List<ActivityInstance> activityInstances = runtimeService.createActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .activityType("userTask")
                .orderByActivityInstanceStartTime().asc()
                .finished().list();
        if (CollUtil.isNotEmpty(activityInstances)) {
            activityInstances.forEach(activityInstance -> {
                ActivityInstanceListOutputDTO activityInstanceListOutputDTO = new ActivityInstanceListOutputDTO(activityInstance);
                // 设置昵称
                final String nickName = actUserService.getNickNameByUserId(activityInstance.getAssignee());
                activityInstanceListOutputDTO.setAssignee(nickName);
                // 设置 审批意义 和 审批状态
                final List<Comment> taskComment = taskService.getTaskComments(activityInstance.getTaskId(), TASK_COMMENT);
                final Comment comment = CollUtil.getFirst(taskComment);
                activityInstanceListOutputDTO.setComment(comment != null ? comment.getFullMessage() : "");
                final List<Comment> taskStatusComments = taskService.getTaskComments(activityInstance.getTaskId(), TASK_STATUS);
                final Comment taskStatus = CollUtil.getFirst(taskStatusComments);
                activityInstanceListOutputDTO.setStatus(taskStatus != null ? taskStatus.getFullMessage() : "");
                activityInstanceListOutputDTOS.add(activityInstanceListOutputDTO);
            });
            return activityInstanceListOutputDTOS;
        } else {
            final List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .orderByHistoricTaskInstanceStartTime().asc()
                    .finished()
                    .list();
            historicTaskInstances.forEach(historicTaskInstance -> {
                ActivityInstanceListOutputDTO activityInstanceListOutputDTO = new ActivityInstanceListOutputDTO(historicTaskInstance);
                // 设置昵称
                final String nickName = actUserService.getNickNameByUserId(historicTaskInstance.getAssignee());
                activityInstanceListOutputDTO.setAssignee(nickName);
                // 设置 审批意义 和 审批状态
                final List<Comment> taskComment = taskService.getTaskComments(historicTaskInstance.getId(), TASK_COMMENT);
                final Comment comment = CollUtil.getFirst(taskComment);
                activityInstanceListOutputDTO.setComment(comment != null ? comment.getFullMessage() : "");
                final List<Comment> taskStatusComments = taskService.getTaskComments(historicTaskInstance.getId(), TASK_STATUS);
                final Comment taskStatus = CollUtil.getFirst(taskStatusComments);
                activityInstanceListOutputDTO.setStatus(taskStatus != null ? taskStatus.getFullMessage() : "");
                activityInstanceListOutputDTOS.add(activityInstanceListOutputDTO);
            });
            return activityInstanceListOutputDTOS;
        }

    }

    /**
     * 获取运行任务节点信息+批注信息
     *
     * @param processInstanceId
     * @return
     */
    public List<ActivityInstanceListOutputDTO> getRunTaskInsList(String processInstanceId) {
        List<ActivityInstanceListOutputDTO> activityInstanceListOutputDTOS = new ArrayList<>();
        final List<ActivityInstance> activityInstances = runtimeService.createActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .activityType("userTask")
                .orderByActivityInstanceStartTime().asc()
                .finished().list();
        activityInstances.forEach(activityInstance -> {
            ActivityInstanceListOutputDTO activityInstanceListOutputDTO = new ActivityInstanceListOutputDTO(activityInstance);
            // 设置昵称
            final String nickName = actUserService.getNickNameByUserId(activityInstance.getAssignee());
            activityInstanceListOutputDTO.setAssignee(nickName);
            // 设置 审批意义 和 审批状态
            final List<Comment> taskComment = taskService.getTaskComments(activityInstance.getId(), TASK_COMMENT);
            final Comment comment = CollUtil.getFirst(taskComment);
            activityInstanceListOutputDTO.setComment(comment != null ? comment.getFullMessage() : "");
            final List<Comment> taskStatusComments = taskService.getTaskComments(activityInstance.getId(), TASK_STATUS);
            final Comment taskStatus = CollUtil.getFirst(taskStatusComments);
            activityInstanceListOutputDTO.setStatus(taskStatus != null ? taskStatus.getFullMessage() : "");
            activityInstanceListOutputDTOS.add(activityInstanceListOutputDTO);
        });
        return activityInstanceListOutputDTOS;
    }

    /**
     * 获取历史任务节点信息+批注信息
     *
     * @param taskId
     * @return
     */
    public List<ActivityInstanceListOutputDTO> getHisTaskInsListByTaskId(String taskId) {
        final HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        List<ActivityInstanceListOutputDTO> activityInstanceListOutputDTOS = new ArrayList<>();
        final List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(taskInstance.getProcessInstanceId())
                .orderByHistoricTaskInstanceStartTime().asc()
                .finished()
                .list();
        historicTaskInstances.forEach(historicTaskInstance -> {
            ActivityInstanceListOutputDTO activityInstanceListOutputDTO = new ActivityInstanceListOutputDTO(historicTaskInstance);
            // 设置昵称
            final String nickName = actUserService.getNickNameByUserId(historicTaskInstance.getAssignee());
            activityInstanceListOutputDTO.setAssignee(nickName);
            // 设置 审批意义 和 审批状态
            final List<Comment> taskComment = taskService.getTaskComments(historicTaskInstance.getId(), TASK_COMMENT);
            final Comment comment = CollUtil.getFirst(taskComment);
            activityInstanceListOutputDTO.setComment(comment != null ? comment.getFullMessage() : "");
            final List<Comment> taskStatusComments = taskService.getTaskComments(historicTaskInstance.getId(), TASK_STATUS);
            final Comment taskStatus = CollUtil.getFirst(taskStatusComments);
            activityInstanceListOutputDTO.setStatus(taskStatus != null ? taskStatus.getFullMessage() : "");
            activityInstanceListOutputDTOS.add(activityInstanceListOutputDTO);
        });
        return activityInstanceListOutputDTOS;
    }

    /**
     * 获取历史任务节点信息+批注信息
     *
     * @param processInstanceId
     * @return
     */
    public List<ActivityInstanceListOutputDTO> getHisTaskInsListByProInsId(String processInstanceId) {
        List<ActivityInstanceListOutputDTO> activityInstanceListOutputDTOS = new ArrayList<>();
        final List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceStartTime().asc()
                .finished()
                .list();
        historicTaskInstances.forEach(historicTaskInstance -> {
            ActivityInstanceListOutputDTO activityInstanceListOutputDTO = new ActivityInstanceListOutputDTO(historicTaskInstance);
            // 设置昵称
            final String nickName = actUserService.getNickNameByUserId(historicTaskInstance.getAssignee());
            activityInstanceListOutputDTO.setAssignee(nickName);
            // 设置 审批意义 和 审批状态
            final List<Comment> taskComment = taskService.getTaskComments(historicTaskInstance.getId(), TASK_COMMENT);
            final Comment comment = CollUtil.getFirst(taskComment);
            activityInstanceListOutputDTO.setComment(comment != null ? comment.getFullMessage() : "");
            final List<Comment> taskStatusComments = taskService.getTaskComments(historicTaskInstance.getId(), TASK_STATUS);
            final Comment taskStatus = CollUtil.getFirst(taskStatusComments);
            activityInstanceListOutputDTO.setStatus(taskStatus != null ? taskStatus.getFullMessage() : "");
            activityInstanceListOutputDTOS.add(activityInstanceListOutputDTO);
        });
        return activityInstanceListOutputDTOS;
    }

    /**
     * 获取代办任务
     *
     * @param taskRunSearchInputDTO
     * @param userId
     * @return
     */
    public List<TaskListOutputDTO> getRunTaskList(TaskRunSearchInputDTO taskRunSearchInputDTO, PageModel pageModel, String userId) {
        final List<String> roleIdsAndDeptId = actUserService.getRoleIdsAndDeptId(userId);

        final TaskQuery taskQuery = taskService.createTaskQuery()
                // 候选组
                .taskCandidateGroupIn(roleIdsAndDeptId)
                // 候选用户 or Assigned
                .taskCandidateOrAssigned(userId)
                .orderByTaskCreateTime().desc()
                .active();
        if (StrUtil.isNotBlank(taskRunSearchInputDTO.getProcessDefinitionName())) {
            taskQuery.processDefinitionNameLike(StrUtil.format("% {} %", taskRunSearchInputDTO.getProcessDefinitionName()));
        }
        if (StrUtil.isNotBlank(taskRunSearchInputDTO.getTaskName())) {
            taskQuery.taskNameLike(StrUtil.format("% {} %", taskRunSearchInputDTO.getTaskName()));
        }
        final long count = taskQuery.count();
        PageUtil.setFirstPageNo(1);
        final List<Task> list = taskQuery
                .listPage(PageUtil.getStart(Convert.toInt(pageModel.getCurrent()), Convert.toInt(pageModel.getSize())), Convert.toInt(pageModel.getSize()));
        List<TaskListOutputDTO> taskListOutputDTOS = new ArrayList<>();
        list.forEach(task -> {
            final ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(task.getProcessDefinitionId())
                    .singleResult();
            final ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            String startUserName = null,
                    curTaskUserName = null;
            if (StrUtil.isNotBlank(processInstance.getStartUserId())) {
                startUserName = actUserService.getNickNameByUserId(processInstance.getStartUserId());
            }
            if (StrUtil.isNotBlank(task.getAssignee())) {
                curTaskUserName = actUserService.getNickNameByUserId(task.getAssignee());
            }
            taskListOutputDTOS.add(new TaskListOutputDTO()
                    .setTaskId(task.getId())
                    .setProcessDefinitionName(processDefinition.getName())
                    .setStartUserId(processInstance.getStartUserId())
                    .setStartUserName(startUserName)
                    .setStartTime(processInstance.getStartTime())
                    .setClaimTime(task.getClaimTime())
                    .setDueDate(task.getDueDate())
                    .setTaskName(task.getName())
                    .setCurTaskUserId(task.getAssignee())
                    .setCurTaskUserName(curTaskUserName)
                    .setPriority(task.getPriority())

            );
        });
        return taskListOutputDTOS;
    }

    /**
     * 获取已办任务
     *
     * @param userId
     * @return
     */
    public List<HisTaskListOutputDTO> getHisTaskList(String userId) {
        final List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userId)
                .orderByHistoricTaskInstanceStartTime().desc()
                .finished()
                .list();
        List<HisTaskListOutputDTO> hisTaskListOutputDTOS = new ArrayList<>();
        list.forEach(historicTaskInstance -> {
            final ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(historicTaskInstance.getProcessDefinitionId())
                    .singleResult();
            final HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(historicTaskInstance.getProcessInstanceId()).singleResult();
            final String nickName = actUserService.getNickNameByUserId(historicProcessInstance.getStartUserId());
            hisTaskListOutputDTOS.add(new HisTaskListOutputDTO()
                    .setTaskId(historicTaskInstance.getId())
                    .setTaskName(historicTaskInstance.getName())
                    .setProcessDefinitionName(processDefinition.getName())
                    .setStartUserId(historicProcessInstance.getStartUserId())
                    .setStartUserName(nickName)
                    .setStartTime(historicProcessInstance.getStartTime())
                    .setEndTime(historicTaskInstance.getEndTime())
                    .setDuration(DateUtil.formatBetween(historicTaskInstance.getCreateTime(), historicTaskInstance.getEndTime(), BetweenFormatter.Level.SECOND)));
        });
        return hisTaskListOutputDTOS;
    }
}

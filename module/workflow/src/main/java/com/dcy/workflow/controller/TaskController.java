package com.dcy.workflow.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.StrUtil;
import com.dcy.common.constant.TaskConstants;
import com.dcy.common.model.PageModel;
import com.dcy.common.model.PageResult;
import com.dcy.common.model.R;
import com.dcy.common.service.ActUserService;
import com.dcy.db.base.controller.RBaseController;
import com.dcy.workflow.dto.input.TaskHisSearchInputDTO;
import com.dcy.workflow.dto.input.TaskRunSearchInputDTO;
import com.dcy.workflow.dto.output.ActivityInstanceListOutputDTO;
import com.dcy.workflow.dto.output.HisTaskListOutputDTO;
import com.dcy.workflow.dto.output.TaskListOutputDTO;
import com.dcy.workflow.service.ActTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author???dcy
 * @Description: ??????????????????
 * @Date: 2021/6/11 15:36
 */
@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping("/flow/task")
@Api(value = "TaskController", tags = {"??????????????????"})
public class TaskController extends RBaseController {

    private final ActTaskService actTaskService;
    private final ActUserService actUserService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final RepositoryService repositoryService;
    private final HistoryService historyService;

    //========================??????????????????===================================

    @ApiOperation(value = "??????????????????????????????")
    @ApiImplicitParam(name = "processInstanceId", value = "????????????id", dataType = "String", paramType = "query")
    @GetMapping(value = "/getRunTaskInsList")
    public R<List<ActivityInstanceListOutputDTO>> getRunTaskInsList(@NotBlank(message = "????????????id????????????") String processInstanceId) {
        return success(actTaskService.getRunTaskInsList(processInstanceId));
    }

    @ApiOperation(value = "????????????id??????????????????????????????")
    @ApiImplicitParam(name = "taskId", value = "??????id", dataType = "String", paramType = "query")
    @GetMapping(value = "/getHisTaskInsListByTaskId")
    public R<List<ActivityInstanceListOutputDTO>> getHisTaskInsListByTaskId(@NotBlank(message = "??????id????????????") String taskId) {
        return success(actTaskService.getHisTaskInsListByTaskId(taskId));
    }

    @ApiOperation(value = "??????????????????id??????????????????????????????")
    @ApiImplicitParam(name = "processInstanceId", value = "????????????id", dataType = "String", paramType = "query")
    @GetMapping(value = "/getHisTaskInsListByProInsId")
    public R<List<ActivityInstanceListOutputDTO>> getHisTaskInsListByProInsId(@NotBlank(message = "????????????id????????????") String processInstanceId) {
        return success(actTaskService.getHisTaskInsListByProInsId(processInstanceId));
    }

    @ApiOperation(value = "??????????????????????????????????????????????????????????????????????????????")
    @ApiImplicitParam(name = "processInstanceId", value = "????????????id", dataType = "String", paramType = "query")
    @GetMapping(value = "/getTaskInsList")
    public R<List<ActivityInstanceListOutputDTO>> getTaskInsList(@NotBlank(message = "????????????id????????????") String processInstanceId) {
        return success(actTaskService.getTaskInsList(processInstanceId));
    }


    //========================????????????===================================

    @ApiOperation(value = "??????????????????")
    @GetMapping(value = "/getRunTaskList")
    public R<PageResult<TaskListOutputDTO>> getRunTaskList(TaskRunSearchInputDTO taskRunSearchInputDTO, PageModel pageModel) {
        PageResult<TaskListOutputDTO> pageResult = new PageResult<>();
        final String userId = getUserId();
        // ??????user????????????
        final List<String> roleIdsAndDeptId = actUserService.getRoleIdsAndDeptId(userId);
        final TaskQuery taskQuery = taskService.createTaskQuery()
                // ?????????
                .taskCandidateGroupIn(roleIdsAndDeptId)
                // ???????????? or Assigned
                .taskCandidateOrAssigned(userId)
                .orderByTaskCreateTime().desc()
                .active();
        if (StrUtil.isNotBlank(taskRunSearchInputDTO.getProcessDefinitionName())) {
            taskQuery.processDefinitionNameLike(StrUtil.format("% {} %", taskRunSearchInputDTO.getProcessDefinitionName()));
        }
        if (StrUtil.isNotBlank(taskRunSearchInputDTO.getTaskName())) {
            taskQuery.taskNameLike(StrUtil.format("% {} %", taskRunSearchInputDTO.getTaskName()));
        }
        // ????????????
        final long count = taskQuery.count();
        if (count <= 0) {
            return success(pageResult);
        }
        PageUtil.setFirstPageNo(1);
        pageResult.setCurrent(pageModel.getCurrent());
        pageResult.setPages(PageUtil.totalPage(Convert.toInt(count), Convert.toInt(pageModel.getSize())));
        final List<Task> list = taskQuery
                .listPage(PageUtil.getStart(Convert.toInt(pageModel.getCurrent()), Convert.toInt(pageModel.getSize())), Convert.toInt(pageModel.getSize()));
        List<TaskListOutputDTO> taskListOutputDTOS = new ArrayList<>();
        list.forEach(task -> {
            final ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(task.getProcessDefinitionId())
                    .singleResult();
            final ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            final TaskListOutputDTO taskListOutputDTO = new TaskListOutputDTO();
            String startUserName = null,
                    curTaskUserName = null;
            if (StrUtil.isNotBlank(processInstance.getStartUserId())) {
                startUserName = actUserService.getNickNameByUserId(processInstance.getStartUserId());
            }
            if (StrUtil.isNotBlank(task.getAssignee())) {
                curTaskUserName = actUserService.getNickNameByUserId(task.getAssignee());
            }
            final String businessKey = taskService.getVariable(task.getId(), TaskConstants.TASK_BUSINESS_KEY, String.class);
            if (StrUtil.isNotBlank(businessKey)) {
                taskListOutputDTO.setBusinessKey(businessKey);
                final List<String> businessKeyAsList = StrUtil.split(businessKey, ":");
                // ????????????
                final String tableName = CollUtil.getFirst(businessKeyAsList);
                taskListOutputDTO.setTableName(tableName);
                // ????????????id
                final String id = CollUtil.getLast(businessKeyAsList);
                taskListOutputDTO.setFormId(id);
            }
            taskListOutputDTOS.add(taskListOutputDTO
                    .setTaskId(task.getId())
                    .setProcessInstanceId(task.getProcessInstanceId())
                    .setProcessInstanceName(processInstance.getName())
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
        pageResult.setRecords(taskListOutputDTOS);
        pageResult.setSize(pageModel.getSize());
        pageResult.setTotal(count);
        return R.success(pageResult);
    }

    @ApiOperation(value = "??????????????????")
    @GetMapping(value = "/getHisTaskList")
    public R<PageResult<HisTaskListOutputDTO>> getHisTaskList(TaskHisSearchInputDTO taskHisSearchInputDTO, PageModel pageModel) {
        PageResult<HisTaskListOutputDTO> pageResult = new PageResult<>();
        final String userId = getUserId();
        final HistoricTaskInstanceQuery taskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userId)
                .orderByHistoricTaskInstanceStartTime().desc()
                .finished();
        if (StrUtil.isNotBlank(taskHisSearchInputDTO.getProcessDefinitionName())) {
            taskInstanceQuery.processDefinitionNameLike(StrUtil.format("% {} %", taskHisSearchInputDTO.getProcessDefinitionName()));
        }
        if (StrUtil.isNotBlank(taskHisSearchInputDTO.getTaskName())) {
            taskInstanceQuery.taskNameLike(StrUtil.format("% {} %", taskHisSearchInputDTO.getTaskName()));
        }
        // ????????????
        final long count = taskInstanceQuery.count();
        if (count <= 0) {
            return success(pageResult);
        }
        PageUtil.setFirstPageNo(1);
        pageResult.setCurrent(pageModel.getCurrent());
        pageResult.setPages(PageUtil.totalPage(Convert.toInt(count), Convert.toInt(pageModel.getSize())));
        final List<HistoricTaskInstance> list = taskInstanceQuery
                .listPage(PageUtil.getStart(Convert.toInt(pageModel.getCurrent()), Convert.toInt(pageModel.getSize())), Convert.toInt(pageModel.getSize()));
        List<HisTaskListOutputDTO> hisTaskListOutputDTOS = new ArrayList<>();
        list.forEach(historicTaskInstance -> {
            final ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(historicTaskInstance.getProcessDefinitionId())
                    .singleResult();
            final HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(historicTaskInstance.getProcessInstanceId()).singleResult();
            final String nickName = actUserService.getNickNameByUserId(historicProcessInstance.getStartUserId());

            final HisTaskListOutputDTO hisTaskListOutputDTO = new HisTaskListOutputDTO();
            // ??????????????????
            final HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery().processInstanceId(historicTaskInstance.getProcessInstanceId())
                    .variableName(TaskConstants.TASK_BUSINESS_KEY).singleResult();
            if (historicVariableInstance != null) {
                final String businessKey = (String) historicVariableInstance.getValue();
                hisTaskListOutputDTO.setBusinessKey(businessKey);
                final List<String> businessKeyAsList = StrUtil.split(businessKey, ":");
                // ????????????
                final String tableName = CollUtil.getFirst(businessKeyAsList);
                hisTaskListOutputDTO.setTableName(tableName);
                // ????????????id
                final String id = CollUtil.getLast(businessKeyAsList);
                hisTaskListOutputDTO.setFormId(id);
            }
            hisTaskListOutputDTOS.add(hisTaskListOutputDTO
                    .setTaskId(historicTaskInstance.getId())
                    .setProcessInstanceId(historicTaskInstance.getProcessInstanceId())
                    .setProcessInstanceName(historicProcessInstance.getName())
                    .setTaskName(historicTaskInstance.getName())
                    .setProcessDefinitionName(processDefinition.getName())
                    .setStartUserId(historicProcessInstance.getStartUserId())
                    .setStartUserName(nickName)
                    .setStartTime(historicProcessInstance.getStartTime())
                    .setEndTime(historicTaskInstance.getEndTime())
                    .setDuration(DateUtil.formatBetween(historicTaskInstance.getCreateTime(), historicTaskInstance.getEndTime(), BetweenFormatter.Level.SECOND)));
        });
        pageResult.setRecords(hisTaskListOutputDTOS);
        pageResult.setSize(pageModel.getSize());
        pageResult.setTotal(count);
        return R.success(pageResult);
    }

    //========================????????????===================================

    @ApiOperation(value = "????????????", notes = "???????????????????????????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "??????ID", dataType = "String", paramType = "query", required = true)
    })
    @PostMapping(value = "/completeTask")
    public R<Boolean> completeTask(@NotBlank(message = "??????id????????????") String taskId) {
        actTaskService.complete(taskId, getUserId());
        return success(true);
    }

    @ApiOperation(value = "????????????????????????????????????+????????????", notes = "????????????????????????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "??????ID", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "comment", value = "????????????", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/completeTaskAndComment")
    public R<Boolean> completeTaskAndComment(@NotBlank(message = "??????id????????????") String taskId,
                                             @NotBlank(message = "????????????????????????") String comment) {
        actTaskService.complete(taskId, getUserId(), comment);
        return success(true);
    }

    @ApiOperation(value = "????????????????????????????????????+????????????+????????????", notes = "?????????????????????????????????????????????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "??????ID", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "comment", value = "????????????", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "adopt", value = "????????????", dataType = "Boolean", paramType = "query")
    })
    @PostMapping(value = "/completeTaskAndCommentAndAdopt")
    public R<Boolean> completeTaskAndCommentAndAdopt(@NotBlank(message = "??????id????????????") String taskId,
                                                     @NotBlank(message = "????????????????????????") String comment,
                                                     @NotNull(message = "????????????????????????") Boolean adopt) {
        actTaskService.complete(taskId, getUserId(), comment, adopt);
        return success(true);
    }

    @ApiOperation(value = "????????????????????????????????????+????????????+????????????+????????????", notes = "???????????????????????????????????????????????????????????????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "??????ID", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "comment", value = "????????????", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "adopt", value = "????????????", dataType = "Boolean", paramType = "query")
    })
    @PostMapping(value = "/completeTaskAndCommentAndSetVar")
    public R<Boolean> completeTaskAndCommentAndSetVar(@NotBlank(message = "??????id????????????") String taskId,
                                                      @NotBlank(message = "????????????????????????") String comment,
                                                      @NotNull(message = "????????????????????????") Boolean adopt) {
        actTaskService.completeAndSetVar(taskId, getUserId(), comment, adopt);
        return success(true);
    }

}

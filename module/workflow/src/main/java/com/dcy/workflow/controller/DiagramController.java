package com.dcy.workflow.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.dcy.workflow.service.ActModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/6/7 10:37
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/flow/diagram")
@Api(value = "DiagramController", tags = {"流程图操作接口"})
public class DiagramController {

    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;
    private final ActModelService actModelService;
    private final HistoryService historyService;
    private final TaskService taskService;
    private final ProcessEngine processEngine;

    @ApiOperation(value = "根据流程定义id获取流程图（不带跟踪的图片）")
    @ApiImplicitParam(name = "processDefinitionId", value = "流程定义id", dataType = "String", paramType = "query", required = true)
    @GetMapping(value = "/getPicByProcessDefinitionId")
    public void getPicByProcessDefinitionId(@NotBlank(message = "流程定义id不能为空") String processDefinitionId, HttpServletResponse response) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        if (processDefinition != null) {
            //获取流程图
            InputStream bmp = actModelService.getBpmnModel(processDefinition.getId());
            ServletUtil.write(response, bmp);
        }
    }


    @ApiOperation(value = "根据流程实例id获取流程图（不带跟踪的图片）")
    @ApiImplicitParam(name = "processInstanceId", value = "流程实例id", dataType = "String", paramType = "query", required = true)
    @GetMapping(value = "/getPicByProcessInstanceId")
    public void getPicByProcessInstanceId(@NotBlank(message = "流程实例id不能为空") String processInstanceId, HttpServletResponse response) {
        // 查询运行得
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (processInstance != null) {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
            if (processDefinition != null) {
                //获取流程图
                InputStream bmp = actModelService.getBpmnModel(processDefinition.getId());
                ServletUtil.write(response, bmp);
            }
        } else {
            // 查询历史得
            final HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            if (historicProcessInstance != null) {
                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult();
                if (processDefinition != null) {
                    //获取流程图
                    InputStream bmp = actModelService.getBpmnModel(processDefinition.getId());
                    ServletUtil.write(response, bmp);
                }
            }
        }
    }

    @ApiOperation(value = "根据任务id获取流程图（带跟踪的图片）")
    @ApiImplicitParam(name = "taskId", value = "任务id", dataType = "String", paramType = "query", required = true)
    @GetMapping(value = "/getTracePicByTaskId")
    public void getTracePicByTaskId(@NotBlank(message = "任务id不能为空") String taskId, HttpServletResponse response) {
        List<String> highLightedActivitis = new ArrayList<>();
        // 根据流程实例获取流程图
        // 流程定义ID
        String processDefinitionId = null;
        // 查看完成的进程中是否存在此进程
        String processInstanceId = null;
        // 根据任务ID获取流程实例ID
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task != null) {
            processInstanceId = task.getProcessInstanceId();
            // 如果流程没有结束，则取当前活动节点
            // 根据流程实例ID获得当前处于活动状态的ActivityId合集
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            processDefinitionId = pi.getProcessDefinitionId();
        } else {
            final HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            if (historicTaskInstance != null) {
                processInstanceId = historicTaskInstance.getProcessInstanceId();
                // 如果流程已经结束，则得到结束节点
                HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
                processDefinitionId = pi.getProcessDefinitionId();
            }
        }
        if (StrUtil.isBlank(processInstanceId) && StrUtil.isBlank(processDefinitionId)) {
            return;
        }
        // 获得活动的节点
        List<HistoricActivityInstance> highLightedActivitList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
        for (HistoricActivityInstance tempActivity : highLightedActivitList) {
            String activityId = tempActivity.getActivityId();
            highLightedActivitis.add(activityId);
        }
        List<String> flows = new ArrayList<>();
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessEngineConfiguration processEngineConfig = processEngine.getProcessEngineConfiguration();

        ProcessDiagramGenerator diagramGenerator = processEngineConfig.getProcessDiagramGenerator();
        InputStream bmp = diagramGenerator.generateDiagram(bpmnModel, "bmp", highLightedActivitis, flows, processEngineConfig.getActivityFontName(),
                processEngineConfig.getLabelFontName(), processEngineConfig.getAnnotationFontName(), processEngineConfig.getClassLoader(), 1.0, true);
        ServletUtil.write(response, bmp);
    }


    @ApiOperation(value = "根据流程实例id获取流程图（带跟踪的图片）")
    @ApiImplicitParam(name = "processInstanceId", value = "流程实例id", dataType = "String", paramType = "query", required = true)
    @GetMapping(value = "/getTracePicByProcessInstanceId")
    public void getTracePicByProcessInstanceId(@NotBlank(message = "流程实例id不能为空") String processInstanceId, HttpServletResponse response) {
        List<String> highLightedActivitis = new ArrayList<>();
        // 根据流程实例获取流程图
        // 流程定义ID
        String processDefinitionId = null;
        final ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (processInstance != null) {
            processDefinitionId = processInstance.getProcessDefinitionId();
            // 获得活动的节点
            runtimeService.createActivityInstanceQuery()
                    .processInstanceId(processInstanceId).orderByActivityInstanceStartTime().asc().list()
                    .forEach(activityInstance -> highLightedActivitis.add(activityInstance.getActivityId()));
        } else {
            final HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            if (historicProcessInstance != null) {
                processDefinitionId = historicProcessInstance.getProcessDefinitionId();
                // 获得活动的节点
                historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list()
                        .forEach(historicActivityInstance -> highLightedActivitis.add(historicActivityInstance.getActivityId()));
            }
        }
        if (StrUtil.isBlank(processInstanceId) && StrUtil.isBlank(processDefinitionId)) {
            return;
        }
        List<String> flows = new ArrayList<>();
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessEngineConfiguration processEngineConfig = processEngine.getProcessEngineConfiguration();

        ProcessDiagramGenerator diagramGenerator = processEngineConfig.getProcessDiagramGenerator();
        InputStream bmp = diagramGenerator.generateDiagram(bpmnModel, "bmp", highLightedActivitis, flows, processEngineConfig.getActivityFontName(),
                processEngineConfig.getLabelFontName(), processEngineConfig.getAnnotationFontName(), processEngineConfig.getClassLoader(), 1.0, true);
        ServletUtil.write(response, bmp);
    }
}


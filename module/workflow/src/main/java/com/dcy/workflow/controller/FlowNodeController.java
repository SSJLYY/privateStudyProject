package com.dcy.workflow.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dcy.common.annotation.Log;
import com.dcy.common.model.R;
import com.dcy.db.base.controller.BaseController;
import com.dcy.workflow.dto.input.FlowNodeSaveInputDTO;
import com.dcy.workflow.dto.output.FlowNodeListOutputDTO;
import com.dcy.workflow.dtomapper.MFlowNodeMapper;
import com.dcy.workflow.listener.TaskCandidateListener;
import com.dcy.workflow.model.FlowNode;
import com.dcy.workflow.service.FlowNodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 流程节点表 前端控制器
 * </p>
 *
 * @author dcy
 * @since 2021-06-08
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/flow/flow-node")
@Api(value = "FlowNodeController", tags = {"流程节点表操作接口"})
public class FlowNodeController extends BaseController<FlowNodeService, FlowNode> {

    private final RepositoryService repositoryService;
    private final MFlowNodeMapper mFlowNodeMapper;

    @Log
    @ApiOperation(value = "保存")
    @PostMapping(value = "/save")
    public R<Boolean> save(@Validated @ApiParam @RequestBody List<FlowNodeSaveInputDTO> flowNodeUpdateInputDTOs) {
        List<FlowNode> flowNodes = mFlowNodeMapper.toFlowNode(flowNodeUpdateInputDTOs);
        return success(baseService.saveFlowNode(flowNodes));
    }

    @ApiOperation(value = "获取活动节点信息")
    @ApiImplicitParam(name = "processDefinitionId", value = "流程定义id", dataType = "String", paramType = "query", required = true)
    @GetMapping(value = "/getActivityListByProDefId")
    public R<List<FlowNodeListOutputDTO>> getActivityListByProDefId(@NotBlank(message = "流程定义id不能为空") String processDefinitionId) {
        // 查询flowable任务节点信息
        List<FlowNodeListOutputDTO> list = new ArrayList<>();
        // 获取流程定义对象
        final ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        // 获取流程图
        final Process process = repositoryService.getBpmnModel(processDefinitionId).getProcessById(processDefinition.getKey());
        final List<UserTask> flowElements = process.findFlowElementsOfType(UserTask.class);
        for (UserTask userTask : flowElements) {
            // 有TaskCandidateListener 监听器
            if (userTask.getTaskListeners().stream().anyMatch(flowableListener ->
                            flowableListener.getImplementation().equals(TaskCandidateListener.class.getName()))) {
                final FlowNode flowNode = baseService.getOne(Wrappers.<FlowNode>lambdaQuery()
                        .eq(FlowNode::getProcDefId, processDefinition.getId())
                        .eq(FlowNode::getFlowId, userTask.getId())
                        .eq(FlowNode::getFlowName, userTask.getName())
                        .last("LIMIT 1"));
                if (flowNode != null) {
                    list.add(new FlowNodeListOutputDTO(flowNode.getFlowId(),
                            processDefinition.getId(),
                            userTask.getId(),
                            userTask.getName(),
                            flowNode.getFlowType(),
                            baseService.getIds(flowNode))
                    );
                } else {
                    list.add(new FlowNodeListOutputDTO(
                            processDefinition.getId(),
                            userTask.getId(),
                            userTask.getName())
                    );
                }
            }
        }
        return success(list);

    }

}

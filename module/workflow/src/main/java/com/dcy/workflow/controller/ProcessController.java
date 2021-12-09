package com.dcy.workflow.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.StrUtil;
import com.dcy.common.model.PageModel;
import com.dcy.common.model.PageResult;
import com.dcy.common.model.R;
import com.dcy.db.base.controller.RBaseController;
import com.dcy.workflow.dto.input.ProcessDefinitionSearchInputDTO;
import com.dcy.workflow.dto.output.ProcessDefinitionListOutputDTO;
import com.dcy.workflow.service.ActTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/6/7 10:24
 */
@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping("/flow/process")
@Api(value = "ProcessController", tags = {"流程操作接口"})
public class ProcessController extends RBaseController {

    private final RepositoryService repositoryService;
    private final ActTaskService actTaskService;


    @ApiOperation(value = "获取流程定义列表")
    @GetMapping(value = "/page")
    public R<PageResult<ProcessDefinitionListOutputDTO>> getProcessDefinitionList(ProcessDefinitionSearchInputDTO processDefinitionSearchInputDTO, PageModel pageModel) {
        PageResult<ProcessDefinitionListOutputDTO> pageResult = new PageResult<>();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionId().orderByProcessDefinitionVersion().desc();
        if (StrUtil.isNotBlank(processDefinitionSearchInputDTO.getName())) {
            processDefinitionQuery.processDefinitionNameLike(StrUtil.format("%{}%", processDefinitionSearchInputDTO.getName()));
        }
        if (StrUtil.isNotBlank(processDefinitionSearchInputDTO.getKey())) {
            processDefinitionQuery.processDefinitionKeyLike(StrUtil.format("%{}%", processDefinitionSearchInputDTO.getKey()));
        }
        List<ProcessDefinitionListOutputDTO> list = new ArrayList<>();
        // 设置首页页码
        PageUtil.setFirstPageNo(1);
        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(PageUtil.getStart(Convert.toInt(pageModel.getCurrent()), Convert.toInt(pageModel.getSize())), Convert.toInt(pageModel.getSize()));
        long count = processDefinitionQuery.count();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
            list.add(new ProcessDefinitionListOutputDTO(processDefinition, deployment));
        }
        // 赋值
        pageResult.setCurrent(pageModel.getCurrent());
        pageResult.setTotal(count);
        pageResult.setSize(pageModel.getSize());
        pageResult.setRecords(list);
        pageResult.setPages(PageUtil.totalPage(Convert.toInt(count), Convert.toInt(pageModel.getSize())));
        return success(pageResult);
    }

    @ApiOperation(value = "删除部署的流程，级联删除流程实例")
    @ApiImplicitParam(name = "deploymentId", value = "流程部署id", dataType = "String", paramType = "query")
    @PostMapping(value = "/delete")
    public R<Boolean> delete(@NotBlank(message = "流程部署id不能为空") String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
        return success(true);
    }

    @ApiOperation(value = "删除流程实例")
    @ApiImplicitParam(name = "processInstanceId", value = "流程实例id", dataType = "String", paramType = "query")
    @PostMapping(value = "/deleteProcIns")
    public R<Boolean> deleteProcIns(@NotBlank(message = "流程实例id不能为空") String processInstanceId) {
        actTaskService.deleteProcess(processInstanceId);
        return success(true);
    }

    @ApiOperation(value = "挂起、激活流程实例")
    @ApiImplicitParam(name = "processDefinitionId", value = "流程定义id", dataType = "String", paramType = "query")
    @PostMapping(value = "/hangChange")
    public R<Boolean> hangChange(@NotBlank(message = "流程定义id不能为空") String processDefinitionId) {
        // 判断挂起状态，true 挂起， false 未挂起
        if (repositoryService.isProcessDefinitionSuspended(processDefinitionId)) {
            // 激活
            repositoryService.activateProcessDefinitionById(processDefinitionId);
        } else {
            // 挂起
            repositoryService.suspendProcessDefinitionById(processDefinitionId);
        }
        return success(true);
    }
}


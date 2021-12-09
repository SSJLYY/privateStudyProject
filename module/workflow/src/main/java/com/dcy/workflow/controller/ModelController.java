package com.dcy.workflow.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.StrUtil;
import com.dcy.common.model.PageModel;
import com.dcy.common.model.R;
import com.dcy.db.base.controller.RBaseController;
import com.dcy.common.model.PageResult;
import com.dcy.workflow.dto.input.ModelCreateInputDTO;
import com.dcy.workflow.dto.input.ModelSearchInputDTO;
import com.dcy.workflow.dto.input.ModelUpdateInputDTO;
import com.dcy.workflow.dto.output.ModelListOutputDTO;
import com.dcy.workflow.service.ActModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/6/7 10:55
 */
@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping("/flow/model")
@Api(value = "ModelController", tags = {"模型操作接口"})
public class ModelController extends RBaseController {

    private final RepositoryService repositoryService;
    private final ActModelService actModelService;

    @ApiOperation(value = "获取模型列表")
    @GetMapping(value = "/page")
    public R<PageResult<ModelListOutputDTO>> page(ModelSearchInputDTO modelSearchInputDTO, PageModel pageModel) {
        PageResult<ModelListOutputDTO> pageResult = new PageResult<>();
        ModelQuery modelQuery = repositoryService.createModelQuery().latestVersion().orderByLastUpdateTime().desc();
        if (StrUtil.isNotBlank(modelSearchInputDTO.getCategory())) {
            modelQuery.modelCategoryLike(StrUtil.format("%{}%", modelSearchInputDTO.getCategory()));
        }
        if (StrUtil.isNotBlank(modelSearchInputDTO.getName())) {
            modelQuery.modelNameLike(StrUtil.format("%{}%", modelSearchInputDTO.getName()));
        }
        if (StrUtil.isNotBlank(modelSearchInputDTO.getKey())) {
            modelQuery.modelKey(modelSearchInputDTO.getKey());
        }
        long count = modelQuery.count();
        PageUtil.setFirstPageNo(1);
        pageResult.setCurrent(pageModel.getCurrent());
        pageResult.setPages(PageUtil.totalPage(Convert.toInt(count), Convert.toInt(pageModel.getSize())));
        List<ModelListOutputDTO> list = new ArrayList<>();
        List<Model> modelList = modelQuery.listPage(PageUtil.getStart(Convert.toInt(pageModel.getCurrent()), Convert.toInt(pageModel.getSize())), Convert.toInt(pageModel.getSize()));
        modelList.forEach(model -> {
            list.add(new ModelListOutputDTO(model));
        });
        pageResult.setRecords(list);
        pageResult.setSize(pageModel.getSize());
        pageResult.setTotal(count);
        return success(pageResult);
    }

    @ApiOperation(value = "创建流程")
    @PostMapping(value = "/create")
    public R<Boolean> createModel(@Validated @ApiParam @RequestBody ModelCreateInputDTO modelSaveInputDTO) {
        boolean success = actModelService.createModel(modelSaveInputDTO.getKey(),
                modelSaveInputDTO.getName(),
                modelSaveInputDTO.getCategory(),
                modelSaveInputDTO.getDescription(),
                modelSaveInputDTO.getJsonXml(),
                modelSaveInputDTO.getSvgXml());
        return success(success);
    }

    @ApiOperation(value = "修改流程")
    @PostMapping(value = "/update")
    public R<Boolean> updateModel(@Validated @ApiParam @RequestBody ModelUpdateInputDTO modelUpdateInputDTO) {
        boolean success = actModelService.updateModel(modelUpdateInputDTO.getKey(),
                modelUpdateInputDTO.getId(),
                modelUpdateInputDTO.getName(),
                modelUpdateInputDTO.getCategory(),
                modelUpdateInputDTO.getDescription(),
                modelUpdateInputDTO.getJsonXml(),
                modelUpdateInputDTO.getSvgXml());
        return success(success);
    }

    @ApiOperation(value = "获取流程json信息")
    @ApiImplicitParam(name = "modelId", value = "模型id", dataType = "String", paramType = "query", required = true)
    @GetMapping(value = "/getModelJsonById")
    public R<String> getModelJsonById(@NotBlank(message = "模型id不能为空") String modelId) {
        return success(actModelService.getModelJsonByModelId(modelId));
    }


    @ApiOperation(value = "部署模型")
    @ApiImplicitParam(name = "modelId", value = "模型id", dataType = "String", paramType = "query")
    @PostMapping(value = "deploy")
    public R<Boolean> deploy(@NotBlank(message = "模型id不能为空") String modelId) {
        return success(actModelService.deployModel(modelId));
    }


    @ApiOperation(value = "删除模型")
    @ApiImplicitParam(name = "modelId", value = "模型id", dataType = "String", paramType = "query")
    @PostMapping(value = "delete")
    public R<Boolean> delete(@NotBlank(message = "模型id不能为空") String modelId) {
        repositoryService.deleteModel(modelId);
        return success(true);
    }

}

package com.dcy.modules.system.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dcy.common.annotation.Log;
import com.dcy.common.model.R;
import com.dcy.db.base.controller.BaseController;
import com.dcy.common.model.PageModel;
import com.dcy.common.model.PageResult;
import com.dcy.modules.system.dto.input.ConfigCreateInputDTO;
import com.dcy.modules.system.dto.input.ConfigSearchInputDTO;
import com.dcy.modules.system.dto.input.ConfigUpdateInputDTO;
import com.dcy.modules.system.dto.output.ConfigListOutputDTO;
import com.dcy.modules.system.dtomapper.MConfigMapper;
import com.dcy.system.model.Config;
import com.dcy.system.service.ConfigService;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 参数配置表 前端控制器
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/system/config")
@ApiSupport(order = 30)
@Api(value = "ConfigController", tags = {"参数配置接口"})
public class ConfigController extends BaseController<ConfigService, Config> {

    private final MConfigMapper mConfigMapper;

    @Log
    @ApiOperation(value = "配置分页查询")
    @GetMapping("/page")
    public R<PageResult<ConfigListOutputDTO>> pageList(ConfigSearchInputDTO configSearchInputDTO, PageModel pageModel) {
        // 转换model
        Config config = mConfigMapper.toConfig(configSearchInputDTO);
        // 获取源对象
        IPage<Config> pageListByListOutputDTO = baseService.pageListByEntity(config, pageModel);
        // 转换新对象
        List<ConfigListOutputDTO> configListOutputDTOS = mConfigMapper.toOutputList(pageListByListOutputDTO.getRecords());
        // 返回业务分页数据
        return success(toPageDTO(pageListByListOutputDTO, configListOutputDTOS));
    }

    @ApiOperation(value = "根据配置key查询配置value")
    @ApiImplicitParam(name = "key", value = "参数键名", dataType = "String", paramType = "query", required = true)
    @GetMapping(value = "/getValueByKey")
    public R<String> getValueByKey(@NotBlank(message = "参数键名不能为空") String key) {
        return success(baseService.getValueByKey(key));
    }

    @Log
    @ApiOperation(value = "添加")
    @PostMapping("/save")
    public R<Boolean> save(@Validated @ApiParam @RequestBody ConfigCreateInputDTO configCreateInputDTO) {
        Config config = mConfigMapper.toConfig(configCreateInputDTO);
        return super.save(config);
    }

    @Log
    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public R<Boolean> update(@Validated @ApiParam @RequestBody ConfigUpdateInputDTO configUpdateInputDTO) {
        Config config = mConfigMapper.toConfig(configUpdateInputDTO);
        return super.update(config);
    }

    @Log
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "id", value = "配置id", dataType = "String", paramType = "query", required = true)
    @PostMapping(value = "/delete")
    public R<Boolean> delete(@NotBlank(message = "配置id不能为空") @RequestParam String id) {
        return super.delete(id);
    }

    @Log
    @ApiOperation(value = "根据list删除")
    @PostMapping(value = "/deleteBatch")
    public R<Boolean> deleteBatch(@NotEmpty(message = "集合不能为空") @ApiParam @RequestBody List<String> idList) {
        return super.deleteBatch(idList);
    }

}

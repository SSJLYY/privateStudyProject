package com.dcy.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dcy.common.annotation.Log;
import com.dcy.common.model.R;
import com.dcy.db.base.controller.BaseController;
import com.dcy.common.model.PageModel;
import com.dcy.common.model.PageResult;
import com.dcy.modules.system.dto.input.DictTypeCreateInputDTO;
import com.dcy.modules.system.dto.input.DictTypeSearchInputDTO;
import com.dcy.modules.system.dto.input.DictTypeUpdateInputDTO;
import com.dcy.modules.system.dto.output.DictTypeListOutputDTO;
import com.dcy.modules.system.dtomapper.MDictTypeMapper;
import com.dcy.system.model.DictType;
import com.dcy.system.service.DictTypeService;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * <p>
 * 字典类型表 前端控制器
 * </p>
 *
 * @author dcy
 * @since 2021-03-17
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/system/dict-type")
@ApiSupport(order = 20)
@Api(value = "DictTypeController", tags = {"字典类型接口"})
public class DictTypeController extends BaseController<DictTypeService, DictType> {

    private final MDictTypeMapper mDictTypeMapper;

    @Log
    @ApiOperation(value = "字典类型分页查询")
    @GetMapping("/page")
    public R<PageResult<DictTypeListOutputDTO>> pageList(DictTypeSearchInputDTO dictTypeSearchInputDTO, PageModel pageModel) {
        // 转换model
        DictType dictType = mDictTypeMapper.dictTypeSearchInputDTOToDictType(dictTypeSearchInputDTO);
        // 获取源对象
        IPage<DictType> pageListByListInputDTO = baseService.pageListByEntity(dictType,pageModel);
        // 转换新对象
        List<DictTypeListOutputDTO> userInfoListOutputDTOS = mDictTypeMapper.dictTypesToDictTypeListOutputDTOs(pageListByListInputDTO.getRecords());
        // 返回业务分页数据
        return success(toPageDTO(pageListByListInputDTO, userInfoListOutputDTOS));
    }

    @Log
    @ApiOperation(value = "添加")
    @PostMapping("/save")
    public R<Boolean> save(@Validated @ApiParam @RequestBody DictTypeCreateInputDTO dictTypeCreateInputDTO) {
        DictType dictType = mDictTypeMapper.dictTypeCreateInputDTOToDictType(dictTypeCreateInputDTO);
        return R.success(baseService.save(dictType));
    }

    @Log
    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public R<Boolean> update(@Validated @ApiParam @RequestBody DictTypeUpdateInputDTO dictTypeUpdateInputDTO) {
        DictType dictType = mDictTypeMapper.dictTypeUpdateInputDTOToDictType(dictTypeUpdateInputDTO);
        return R.success(baseService.updateById(dictType));
    }

    @Log
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "id", value = "字典id", dataType = "String", paramType = "query", required = true)
    @PostMapping(value = "/delete")
    public R<Boolean> delete(@NotBlank(message = "字典id不能为空") @RequestParam String id) {
        return success(baseService.deleteDictTypeById(id));
    }

}

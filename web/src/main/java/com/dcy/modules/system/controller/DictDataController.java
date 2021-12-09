package com.dcy.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dcy.common.annotation.Log;
import com.dcy.common.model.R;
import com.dcy.common.utils.TreeUtil;
import com.dcy.db.base.controller.BaseController;
import com.dcy.common.model.PageModel;
import com.dcy.common.model.PageResult;
import com.dcy.modules.system.dto.input.DictDataCreateInputDTO;
import com.dcy.modules.system.dto.input.DictDataSearchInputDTO;
import com.dcy.modules.system.dto.input.DictDataUpdateInputDTO;
import com.dcy.modules.system.dto.output.DictDataListOutputDTO;
import com.dcy.modules.system.dto.output.DictDataSelOutputDTO;
import com.dcy.modules.system.dto.output.DictDataTreeSelOutputDTO;
import com.dcy.modules.system.dtomapper.MDictDataMapper;
import com.dcy.system.model.DictData;
import com.dcy.system.service.DictDataService;
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
 * 字典数据表 前端控制器
 * </p>
 *
 * @author dcy
 * @since 2021-03-17
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/system/dict-data")
@ApiSupport(order = 25)
@Api(value = "DictDataController", tags = {"字典数据接口"})
public class DictDataController extends BaseController<DictDataService, DictData> {

    private final MDictDataMapper mDictDataMapper;

    @Log
    @ApiOperation(value = "字典类型分页查询")
    @GetMapping("/page")
    public R<PageResult<DictDataListOutputDTO>> pageList(DictDataSearchInputDTO dictDataSearchInputDTO, PageModel pageModel) {
        // 转换model
        DictData dictData = mDictDataMapper.dictDataSearchInputDTOToDictData(dictDataSearchInputDTO);
        // 获取源对象
        IPage<DictData> pageListByListInputDTO = baseService.pageListByEntity(dictData, pageModel);
        // 转换新对象
        List<DictDataListOutputDTO> userInfoListOutputDTOS = mDictDataMapper.dictDatasToDictDataListOutputDTOs(pageListByListInputDTO.getRecords());
        // 返回业务分页数据
        return success(toPageDTO(pageListByListInputDTO, userInfoListOutputDTOS));
    }

    @Log
    @ApiOperation(value = "添加")
    @PostMapping("/save")
    public R<Boolean> save(@Validated @ApiParam @RequestBody DictDataCreateInputDTO dictDataCreateInputDTO) {
        DictData dictData = mDictDataMapper.dictDataCreateInputDTOToDictData(dictDataCreateInputDTO);
        return R.success(baseService.save(dictData));
    }

    @Log
    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public R<Boolean> update(@Validated @ApiParam @RequestBody DictDataUpdateInputDTO dictDataUpdateInputDTO) {
        DictData dictData = mDictDataMapper.dictDataUpdateInputDTOToDictData(dictDataUpdateInputDTO);
        return R.success(baseService.updateById(dictData));
    }

    @Log
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "id", value = "字典数据id", dataType = "String", paramType = "query", required = true)
    @PostMapping(value = "/delete")
    public R<Boolean> delete(@NotBlank(message = "字典数据id不能为空") @RequestParam String id) {
        return super.delete(id);
    }

    @ApiOperation(value = "根据类型查询字典项")
    @ApiImplicitParam(name = "dictType", value = "字典类型", dataType = "String", paramType = "query", required = true)
    @GetMapping(value = "/getDictDataListByDictType")
    public R<List<DictDataSelOutputDTO>> getDictDataListByDictType(@NotBlank(message = "字典类型不能为空") @RequestParam String dictType) {
        List<DictData> dictList = baseService.getDictDataListByDictType(dictType);
        List<DictDataSelOutputDTO> dictListOutputDTOS = mDictDataMapper.dictDatasToDictDataSelOutputDTOs(dictList);
        return success(dictListOutputDTOS);
    }

    @ApiOperation(value = "根据类型查询字典项（tree数据）")
    @ApiImplicitParam(name = "dictType", value = "字典类型", dataType = "String", paramType = "query", required = true)
    @GetMapping(value = "/getDictDataTreeListByDictType")
    public R<List<DictDataTreeSelOutputDTO>> getDictDataTreeListByDictType(@NotBlank(message = "字典类型不能为空") @RequestParam String dictType) {
        List<DictData> dictList = baseService.getDictDataListByDictType(dictType);
        List<DictData> dictData = TreeUtil.listToTree(dictList);
        List<DictDataTreeSelOutputDTO> dictListOutputDTOS = mDictDataMapper.dictDatasToDictDataTreeSelOutputDTOs(dictData);
        return success(dictListOutputDTOS);
    }
}

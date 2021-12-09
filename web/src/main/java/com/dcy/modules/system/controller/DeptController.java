package com.dcy.modules.system.controller;

import com.dcy.common.annotation.Log;
import com.dcy.common.model.R;
import com.dcy.db.base.controller.BaseController;
import com.dcy.modules.system.dto.input.DeptCreateInputDTO;
import com.dcy.modules.system.dto.input.DeptSearchInputDTO;
import com.dcy.modules.system.dto.input.DeptUpdateInputDTO;
import com.dcy.modules.system.dto.output.DeptListOutputDTO;
import com.dcy.modules.system.dtomapper.MDeptMapper;
import com.dcy.system.model.Dept;
import com.dcy.system.service.DeptService;
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
 * 部门表 前端控制器
 * </p>
 *
 * @author dcy
 * @since 2021-03-16
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/system/dept")
@ApiSupport(order = 15)
@Api(value = "DeptController", tags = {"部门管理接口"})
public class DeptController extends BaseController<DeptService, Dept> {

    private final MDeptMapper mDeptMapper;

    /**
     * SELECT t.id FROM sys_dept t WHERE FIND_IN_SET(1, ancestors)
     * where u.dept_id = 100 OR u.dept_id IN  (SELECT t.dept_id FROM sys_dept t WHERE FIND_IN_SET(100, ancestors)
     *
     * @param deptSearchInputDTO
     * @return
     */
    @Log
    @ApiOperation(value = "获取部门tree列表数据")
    @GetMapping("/getDeptTreeList")
    public R<List<DeptListOutputDTO>> getDeptTreeList(DeptSearchInputDTO deptSearchInputDTO) {
        Dept dept = mDeptMapper.toDept(deptSearchInputDTO);
        List<Dept> deptTreeTableList = baseService.getDeptTreeList(dept);
        List<DeptListOutputDTO> deptListOutputDTOS = mDeptMapper.toOutputList(deptTreeTableList);
        return R.success(deptListOutputDTOS);
    }

    @Log
    @ApiOperation(value = "添加")
    @PostMapping("/save")
    public R<Boolean> save(@Validated @ApiParam @RequestBody DeptCreateInputDTO deptCreateInputDTO) {
        Dept dept = mDeptMapper.toDept(deptCreateInputDTO);
        return super.save(dept);
    }

    @Log
    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public R<Boolean> update(@Validated @ApiParam @RequestBody DeptUpdateInputDTO deptUpdateInputDTO) {
        Dept dept = mDeptMapper.toDept(deptUpdateInputDTO);
        return super.update(dept);
    }

    @Log
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "id", value = "部门id", dataType = "String", paramType = "query", required = true)
    @PostMapping(value = "/delete")
    public R<Boolean> delete(@NotBlank(message = "部门id不能为空") @RequestParam String id) {
        return super.delete(id);
    }

}

package com.dcy.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dcy.common.annotation.Log;
import com.dcy.common.model.PageModel;
import com.dcy.common.model.PageResult;
import com.dcy.common.model.R;
import com.dcy.db.base.controller.BaseController;
import com.dcy.modules.system.dto.input.RoleCreateInputDTO;
import com.dcy.modules.system.dto.input.RoleResourceInputDTO;
import com.dcy.modules.system.dto.input.RoleSearchInputDTO;
import com.dcy.modules.system.dto.input.RoleUpdateInputDTO;
import com.dcy.modules.system.dto.output.RoleListOutputDTO;
import com.dcy.modules.system.dtomapper.MRoleMapper;
import com.dcy.modules.system.service.AuthService;
import com.dcy.system.model.Role;
import com.dcy.system.service.RoleService;
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
 * @Author：dcy
 * @Description:
 * @Date: 2020/8/26 9:29
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/system/role")
@ApiSupport(order = 5)
@Api(value = "RoleController", tags = {"角色接口"})
public class RoleController extends BaseController<RoleService, Role> {

    private final MRoleMapper mRoleMapper;
    private final AuthService authService;

    @Log
    @ApiOperation(value = "角色分页查询")
    @GetMapping(value = "/page")
    public R<PageResult<RoleListOutputDTO>> page(RoleSearchInputDTO roleSearchInputDTO, PageModel pageModel) {
        // 转换model
        Role role = mRoleMapper.roleSearchInputDTOToRole(roleSearchInputDTO);
        // 获取源对象
        IPage<Role> pageListByEntity = baseService.pageListByEntity(role, pageModel);
        // 转换新对象
        List<RoleListOutputDTO> roleListOutputDTOS = mRoleMapper.rolesToRoleListOutputDTOs(pageListByEntity.getRecords());
        // 返回业务分页数据
        return success(toPageDTO(pageListByEntity, roleListOutputDTOS));
    }

    @Log
    @ApiOperation(value = "获取全部信息")
    @GetMapping(value = "/all")
    public R<List<RoleListOutputDTO>> getAllRoleList() {
        List<Role> roleList = baseService.list();
        List<RoleListOutputDTO> roleListOutputDTOS = mRoleMapper.rolesToRoleListOutputDTOs(roleList);
        return success(roleListOutputDTOS);
    }

    @Log
    @ApiOperation(value = "添加")
    @PostMapping(value = "/save")
    public R<Boolean> save(@Validated @ApiParam @RequestBody RoleCreateInputDTO roleCreateInputDTO) {
        Role role = mRoleMapper.roleCreateInputDTOToRole(roleCreateInputDTO);
        return success(baseService.saveRole(role, roleCreateInputDTO.getDeptIds()));
    }

    @Log
    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public R<Boolean> update(@Validated @ApiParam @RequestBody RoleUpdateInputDTO roleUpdateInputDTO) {
        Role role = mRoleMapper.roleUpdateInputDTOToRole(roleUpdateInputDTO);
        return success(baseService.saveRole(role, roleUpdateInputDTO.getDeptIds()));
    }

    @Log
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "id", value = "角色id", dataType = "String", paramType = "query", required = true)
    @PostMapping(value = "/delete")
    public R<Boolean> delete(@NotBlank(message = "角色id不能为空") @RequestParam String id) {
        return success(baseService.deleteRole(id));
    }

    @Log
    @ApiOperation(value = "根据list删除")
    @PostMapping(value = "/deleteBatch")
    public R<Boolean> deleteBatch(@NotEmpty(message = "集合不能为空") @ApiParam @RequestBody List<String> idList) {
        return success(baseService.deleteBatchRole(idList));
    }

    @Log
    @ApiOperation(value = "保存授权权限")
    @PostMapping(value = "/saveAuthResource")
    public R<Boolean> saveAuthResource(@Validated @ApiParam @RequestBody RoleResourceInputDTO roleResourceInputDto) {
        Boolean bool = baseService.saveAuthResource(roleResourceInputDto.getRoleId(), roleResourceInputDto.getResIds());
        if (bool) {
            // 只能刷新当前登录人的权限，不能修改角色下所有用户的权限
            authService.refreshUserInfoByUserId(roleResourceInputDto.getUserId());
        }
        return success(bool);
    }

    @Log
    @ApiOperation(value = "获取已授权的数据范围部门ids")
    @ApiImplicitParam(name = "roleId", value = "角色Id", dataType = "String", paramType = "query", required = true)
    @GetMapping(value = "/getDataScopeDeptIdsByRoleId")
    public R<List<String>> getDataScopeDeptIdsByRoleId(@NotBlank(message = "角色id不能为空") @RequestParam String roleId) {
        return success(baseService.getDataScopeDeptIdsByRoleId(roleId));
    }
}

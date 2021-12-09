package com.dcy.modules.system.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dcy.common.annotation.Log;
import com.dcy.common.enums.FlowTypeEnum;
import com.dcy.common.model.PageModel;
import com.dcy.common.model.PageResult;
import com.dcy.common.model.R;
import com.dcy.common.utils.BPwdEncoderUtil;
import com.dcy.common.utils.EasyExcelUtil;
import com.dcy.db.base.controller.BaseController;
import com.dcy.db.base.util.ViewMapper;
import com.dcy.enums.AdminApiErrorCode;
import com.dcy.modules.system.dto.input.*;
import com.dcy.modules.system.dto.output.*;
import com.dcy.modules.system.dtomapper.MRoleMapper;
import com.dcy.modules.system.dtomapper.MUserInfoMapper;
import com.dcy.modules.system.service.AuthService;
import com.dcy.system.model.Dept;
import com.dcy.system.model.Role;
import com.dcy.system.model.UserInfo;
import com.dcy.system.service.DeptService;
import com.dcy.system.service.RoleService;
import com.dcy.system.service.UserInfoService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/8/26 9:09
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/system/user")
@ApiSupport(order = 2)
@Api(value = "UserController", tags = {"用户接口"})
public class UserController extends BaseController<UserInfoService, UserInfo> {

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private final MUserInfoMapper mUserInfoMapper;
    private final MRoleMapper mRoleMapper;
    private final AuthService authService;
    private final RoleService roleService;
    private final DeptService deptService;

    @Log
    @ApiOperation(value = "用户分页查询")
    @ApiOperationSupport(order = 1, author = "dcy")
    @GetMapping("/page")
    public R<PageResult<UserInfoListOutputDTO>> page(UserInfoSearchInputDTO userInfoSearchInputDTO, PageModel pageModel) {
        // 转换model
        UserInfo userInfo = mUserInfoMapper.userInfoSearchInputDTOToUserInfo(userInfoSearchInputDTO);
        // 获取源对象
        IPage<UserInfo> pageListByListInputDTO = baseService.pageUserList(userInfo, pageModel);
        // 转换新对象
        List<UserInfoListOutputDTO> userInfoListOutputDTOS = mUserInfoMapper.userInfosToUserInfoListOutputDTOs(pageListByListInputDTO.getRecords());
        // 返回业务分页数据
        return success(toPageDTO(pageListByListInputDTO, userInfoListOutputDTOS));
    }

    @Log
    @ApiOperation(value = "用户分页查询-lamda方式")
    @ApiOperationSupport(order = 2, author = "dcy")
    @GetMapping("/pageList")
    public R<PageResult<UserInfoListOutputDTO>> pageList(UserInfoSearchInputDTO userInfoSearchInputDTO, PageModel pageModel) {
        // 转换model
        UserInfo userInfo = mUserInfoMapper.userInfoSearchInputDTOToUserInfo(userInfoSearchInputDTO);
        // 获取源对象
        IPage<UserInfo> pageListByListInputDTO = baseService.pageList(userInfo, pageModel);
        // 转换新对象
        List<UserInfoListOutputDTO> userInfoListOutputDTOS = mUserInfoMapper.userInfosToUserInfoListOutputDTOs(pageListByListInputDTO.getRecords());

        ViewMapper.batchMapView(userInfoListOutputDTOS,
                ViewMapper.setView(
                        this::getMapDept,
                        UserInfoListOutputDTO::getDeptId,
                        ((userInfoListOutputDTO, dept) -> userInfoListOutputDTO.setDeptName(dept.getName()))));
        // 返回业务分页数据
        return success(toPageDTO(pageListByListInputDTO, userInfoListOutputDTOS));
    }

    private Map<String, Dept> getMapDept(Collection<String> ids) {
        return deptService.listByIds(ids)
                .stream().collect(Collectors.toMap(Dept::getId, dept2 -> dept2));
    }

    @Log
    @ApiOperation(value = "添加")
    @ApiOperationSupport(order = 5, author = "dcy")
    @PostMapping("/save")
    public R<Boolean> save(@Validated @ApiParam @RequestBody UserInfoCreateInputDTO userInfoCreateInputDTO) {
        UserInfo userInfo = mUserInfoMapper.userInfoCreateInputDTOToUserInfo(userInfoCreateInputDTO);
        userInfo.setPassword(PASSWORD_ENCODER.encode(userInfoCreateInputDTO.getPassword()));
        return R.success(baseService.saveUser(userInfo, userInfoCreateInputDTO.getPostIds()));
    }

    @Log
    @ApiOperation(value = "修改")
    @ApiOperationSupport(order = 10, author = "dcy")
    @PostMapping(value = "/update")
    public R<Boolean> update(@Validated @ApiParam @RequestBody UserInfoUpdateInputDTO userInfoUpdateInputDTO) {
        UserInfo userInfo = mUserInfoMapper.userInfoUpdateInputDTOToUserInfo(userInfoUpdateInputDTO);
        return R.success(baseService.saveUser(userInfo, userInfoUpdateInputDTO.getPostIds()));
    }

    @Log
    @ApiOperation(value = "删除")
    @ApiOperationSupport(order = 15, author = "dcy")
    @ApiImplicitParam(name = "id", value = "用户id", dataType = "String", paramType = "query", required = true)
    @PostMapping(value = "/delete")
    public R<Boolean> delete(@NotBlank(message = "用户id不能为空") @RequestParam String id) {
        return super.delete(id);
    }

    @Log
    @ApiOperation(value = "根据list删除")
    @ApiOperationSupport(order = 20, author = "dcy")
    @PostMapping(value = "/deleteBatch")
    public R<Boolean> deleteBatch(@NotEmpty(message = "集合不能为空") @ApiParam @RequestBody List<String> idList) {
        return super.deleteBatch(idList);
    }

    @Log
    @ApiOperation(value = "重置密码")
    @ApiOperationSupport(order = 25, author = "dcy")
    @PostMapping(value = "/resetPassword")
    public R<Boolean> resetPassword(@Validated @ApiParam @RequestBody UserInfoResetPassInputDTO userInfoResetPassInputDTO) {
        UserInfo userInfo = mUserInfoMapper.userInfoResetPassInputDTOToUserInfo(userInfoResetPassInputDTO);
        userInfo.setPassword(PASSWORD_ENCODER.encode(userInfoResetPassInputDTO.getPassword()));
        return super.update(userInfo);
    }

    @Log
    @ApiOperation(value = "根据用户名获取用户信息")
    @ApiOperationSupport(order = 30, author = "dcy")
    @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query", required = true)
    @GetMapping(value = "/getUserInfoByUsername")
    public R<UserInfoListOutputDTO> getUserInfoByUsername(@NotBlank(message = "用户名不能为空") @RequestParam String username) {
        UserInfo userInfo = baseService.getUserInfoByUsername(username);
        UserInfoListOutputDTO userInfoListOutputDTO = mUserInfoMapper.userInfoToUserInfoListOutputDTO(userInfo);
        return success(userInfoListOutputDTO);
    }

    @Log
    @ApiOperation(value = "获取已授权的角色列表")
    @ApiOperationSupport(order = 35, author = "dcy")
    @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "String", paramType = "query", required = true)
    @GetMapping(value = "/getAuthRoleListByUserId")
    public R<List<RoleListOutputDTO>> getAuthRoleListByUserId(@NotBlank(message = "用户id不能为空") @RequestParam String userId) {
        List<Role> roleListByUserId = baseService.getAuthRoleListByUserId(userId);
        List<RoleListOutputDTO> roleListOutputDTOS = mRoleMapper.rolesToRoleListOutputDTOs(roleListByUserId);
        return success(roleListOutputDTOS);
    }

    @Log
    @ApiOperation(value = "保存授权角色")
    @ApiOperationSupport(order = 40, author = "dcy")
    @PostMapping(value = "/saveAuthRole")
    public R<Boolean> saveAuthRole(@Validated @ApiParam @RequestBody UserInfoRoleOutputDTO userInfoRoleOutputDto) {
        Boolean bool = baseService.saveAuthRole(userInfoRoleOutputDto.getUserId(), userInfoRoleOutputDto.getRoleIds());
        if (bool) {
            authService.refreshUserInfoByUserId(userInfoRoleOutputDto.getUserId());
        }
        return success(bool);
    }

    @Log
    @ApiOperation(value = "获取用户岗位列表")
    @ApiOperationSupport(order = 45, author = "dcy")
    @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "String", paramType = "query", required = true)
    @GetMapping(value = "/getPostListByUserId")
    public R<List<String>> getPostListByUserId(@NotBlank(message = "用户id不能为空") @RequestParam String userId) {
        return success(baseService.getPostListByUserId(userId));
    }

    @Log
    @ApiOperation(value = "修改基本信息")
    @ApiOperationSupport(order = 50, author = "dcy")
    @PostMapping(value = "/updateInfo")
    public R<Boolean> updateInfo(@Validated @ApiParam @RequestBody UserInfoUpdateInfoInputDTO userInfoUpdateInfoInputDTO) {
        UserInfo userInfo = mUserInfoMapper.userInfoUpdateInfoInputDTOToUserInfo(userInfoUpdateInfoInputDTO);
        return R.success(baseService.updateById(userInfo));
    }

    @Log
    @PostMapping("/updatePass")
    @ApiOperation(value = "修改密码")
    @ApiOperationSupport(order = 55, author = "dcy")
    public R<Boolean> updatePass(@Validated @ApiParam @RequestBody UserUpdatePassInputDTO userUpdatePassInputDTO) {
        UserInfo userInfo = baseService.getById(userUpdatePassInputDTO.getUserId());
        if (!BPwdEncoderUtil.matches(userUpdatePassInputDTO.getCurrentPass(), userInfo.getPassword())) {
            return R.error(AdminApiErrorCode.USER_UPDATE_PASS_ERROR);
        }
        if (!userUpdatePassInputDTO.getNewPass().equalsIgnoreCase(userUpdatePassInputDTO.getConfPass())) {
            return R.error(AdminApiErrorCode.USER_UPDATE_PASS2_ERROR);
        }
        UserInfo updatePassUser = new UserInfo();
        updatePassUser.setId(userUpdatePassInputDTO.getUserId());
        updatePassUser.setPassword(PASSWORD_ENCODER.encode(userUpdatePassInputDTO.getNewPass()));
        return R.success(baseService.updateById(updatePassUser));
    }

    @GetMapping("/getOnLineUserList")
    @ApiOperation(value = "获取在线用户列表")
    @ApiOperationSupport(order = 60, author = "dcy")
    public R<List<UserInfo>> getOnLineUserList() {
        List<String> list = StpUtil.searchSessionId("", 0, 100);
        final int index = 3;
        List<String> collect = list.stream().map(s -> {
            List<String> split = StrUtil.split(s, ':');
            if (StrUtil.isNotBlank(CollUtil.get(split, index))) {
                return CollUtil.get(split, index);
            }
            return null;
        }).collect(Collectors.toList());
        return R.success(baseService.listByIds(collect));
    }

    @ApiOperation(value = "获取活动节点信息")
    @ApiImplicitParam(name = "flowType", value = "流程审批类型", dataType = "String", paramType = "query", required = true)
    @GetMapping(value = "/getOptionDataByFlowType")
    public R<List<OptionDataListOutputDTO>> getOptionDataByFlowType(String flowType, String search) {
        List<OptionDataListOutputDTO> list = new ArrayList<>();
        FlowTypeEnum flowTypeEnum = FlowTypeEnum.getByCode(flowType);
        if (flowTypeEnum != null) {
            switch (flowTypeEnum) {
                case USER:
                    baseService.list(Wrappers.<UserInfo>lambdaQuery().like(StrUtil.isNotBlank(search), UserInfo::getNickName, search))
                            .forEach(userInfo -> list.add(new OptionDataListOutputDTO(userInfo.getId(), userInfo.getNickName())));
                    break;
                case ROLE:
                    roleService.list(Wrappers.<Role>lambdaQuery().like(StrUtil.isNotBlank(search), Role::getRoleName, search))
                            .forEach(role -> list.add(new OptionDataListOutputDTO(role.getId(), role.getRoleName())));
                    break;
                case DEPT:
                    deptService.list(Wrappers.<Dept>lambdaQuery().like(StrUtil.isNotBlank(search), Dept::getName, search))
                            .forEach(dept -> list.add(new OptionDataListOutputDTO(dept.getId(), dept.getName())));
                    break;
                default:
            }
        }
        return success(list);
    }

    @ApiOperation(value = "导出excel测试")
    @ApiOperationSupport(order = 65, author = "dcy")
    @GetMapping("/exportExcel")
    public void download(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
        List<UserInfo> userInfos = baseService.list();
        List<UserInfoListExcelOutputDTO> userInfoListExcelOutputDTOS = mUserInfoMapper.toExcel(userInfos);
        EasyExcelUtil.normalExcel(userInfoListExcelOutputDTOS,
                UserInfoListExcelOutputDTO.class,
                new ExportParams("用户列表", "sheet", ExcelType.XSSF),
                "用户列表", modelMap, request, response);
    }
}

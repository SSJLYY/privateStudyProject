package com.dcy.modules.system.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.dcy.common.constant.Constant;
import com.dcy.common.model.R;
import com.dcy.common.utils.BPwdEncoderUtil;
import com.dcy.db.base.controller.BaseController;
import com.dcy.enums.AdminApiErrorCode;
import com.dcy.modules.system.dto.input.LoginInputDTO;
import com.dcy.modules.system.dto.output.LoginOutputDTO;
import com.dcy.modules.system.dtomapper.MUserInfoMapper;
import com.dcy.modules.system.service.AuthService;
import com.dcy.system.enums.UserInfoStatusEnum;
import com.dcy.system.model.Role;
import com.dcy.system.model.UserInfo;
import com.dcy.system.service.DeptService;
import com.dcy.system.service.UserInfoService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dcy
 * @Date: 2019/3/22 10:17
 * @Description:
 */
@RequiredArgsConstructor
@RestController
@ApiSupport(order = 1)
@Api(value = "LoginController", tags = {"登录接口"})
@Slf4j
public class LoginController extends BaseController<UserInfoService, UserInfo> {

    private final MUserInfoMapper mUserInfoMapper;
    private final AuthService authService;
    private final DeptService deptService;

    /**
     * 登录
     *
     * @param loginInputDTO
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    @ApiOperationSupport(order = 1, author = "dcy")
    public R<String> login(@Validated @ApiParam @RequestBody LoginInputDTO loginInputDTO) {
        // ----------------可以不调用------------------------
        UserInfo userInfo = baseService.getUserInfoByUsername(loginInputDTO.getUsername());
        if (userInfo == null) {
            return R.error(AdminApiErrorCode.USER_PASSWORD_ERROR);
        }
        if (UserInfoStatusEnum.DISABLE.code.equals(userInfo.getUserStatus())) {
            return R.error(AdminApiErrorCode.USER_LOCKED_ERROR);
        }
        if (!BPwdEncoderUtil.matches(loginInputDTO.getPassword(), userInfo.getPassword())) {
            return R.error(AdminApiErrorCode.USER_PASSWORD_ERROR);
        }
        LoginOutputDTO loginOutputDTO = mUserInfoMapper.userInfoToLoginOutputDTO(userInfo);
        // 设置权限
        loginOutputDTO.setResources(authService.getAuthRoleAndResourceByUserId(userInfo.getId()));
        if (StrUtil.isNotBlank(userInfo.getDeptId())) {
            loginOutputDTO.setDeptName(deptService.getById(userInfo.getDeptId()).getName());
        }
        List<Role> authRoleListByUserId = baseService.getAuthRoleListByUserId(userInfo.getId());
        if (CollUtil.isNotEmpty(authRoleListByUserId)) {
            loginOutputDTO.setRoleName(CollUtil.join(authRoleListByUserId.stream().map(Role::getRoleName).collect(Collectors.toList()), ","));
        }
        // sa-token 登录
        StpUtil.login(userInfo.getId());
        // 获取token
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        // 获取session
        SaSession session = StpUtil.getSession();
        // 设置用户信息
        session.set(Constant.SESSION_USER_KEY, loginOutputDTO);
        return success(tokenInfo.getTokenValue());
    }


    @GetMapping("/getUserInfo")
    @ApiOperation(value = "获取用户信息")
    @ApiOperationSupport(order = 5, author = "dcy")
    public R<LoginOutputDTO> getUserInfo() {
        SaSession session = StpUtil.getSession();
        LoginOutputDTO loginOutputDTO = session.getModel(Constant.SESSION_USER_KEY, LoginOutputDTO.class);
        return success(loginOutputDTO);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "用户退出")
    @ApiOperationSupport(order = 10, author = "dcy")
    public R<String> logout() {
        StpUtil.logout();
        return success("退出成功");
    }
}

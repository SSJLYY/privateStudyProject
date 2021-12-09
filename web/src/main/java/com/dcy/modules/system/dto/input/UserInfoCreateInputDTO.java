package com.dcy.modules.system.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/12/17 8:11
 */
@Getter
@Setter
@ApiModel(value="UserInfoCreateInputDTO", description="用户添加")
public class UserInfoCreateInputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部门id")
    private String deptId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户类型（0、管理员；1、普通用户）")
    private String userType;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    @ApiModelProperty(value = "性别（0、男；1、女）")
    private String sex;

    @ApiModelProperty(value = "头像")
    private String avatarPath;

    @ApiModelProperty(value = "帐号状态（0、正常；1、禁用）")
    private String userStatus;

    @ApiModelProperty(value = "岗位ids")
    private List<String> postIds;
}

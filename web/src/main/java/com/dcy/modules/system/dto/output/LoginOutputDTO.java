package com.dcy.modules.system.dto.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

/**
 * @Author：dcy
 * @Description: 登录使用
 * @Date: 2020/12/17 7:56
 */
@Getter
@Setter
@ApiModel(value="LoginOutputDTO", description="登录返回用户信息")
public class LoginOutputDTO {

    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "部门id")
    private String deptId;

    @ApiModelProperty(value = "用户名")
    private String username;

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

    @ApiModelProperty(value = "资源信息")
    private Set<String> resources;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;
}

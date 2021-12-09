package com.dcy.modules.system.dto.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/10/23 9:18
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel(value = "UserInfoListOutputDTO", description = "用户分页表格")
public class UserInfoListOutputDTO {

    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "部门id")
    private String deptId;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

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


}

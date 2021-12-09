package com.dcy.modules.system.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/12/17 8:11
 */
@Getter
@Setter
@ApiModel(value = "UserInfoSearchInputDTO", description = "用户添加")
public class UserInfoSearchInputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部门id")
    private String deptId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

}

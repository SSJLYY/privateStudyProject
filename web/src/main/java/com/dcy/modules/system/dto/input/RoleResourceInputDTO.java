package com.dcy.modules.system.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：dcy
 * @Description: 授权权限使用
 * @Date: 2020/8/26 9:44
 */
@Getter
@Setter
@ApiModel(value="RoleResourceInputDTO", description="授权权限使用")
public class RoleResourceInputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "角色Id")
    private String roleId;

    @ApiModelProperty(value = "授权权限Ids")
    private List<String> resIds;
}

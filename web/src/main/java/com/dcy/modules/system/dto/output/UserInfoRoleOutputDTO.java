package com.dcy.modules.system.dto.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author：dcy
 * @Description: 授权角色使用
 * @Date: 2020/8/26 9:19
 */
@Getter
@Setter
@ApiModel(value = "UserInfoRoleOutputDTO", description = "授权角色使用")
public class UserInfoRoleOutputDTO {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "授权角色Ids")
    private List<String> roleIds;
}

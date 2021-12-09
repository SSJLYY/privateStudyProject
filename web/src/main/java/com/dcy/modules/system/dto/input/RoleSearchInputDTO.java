package com.dcy.modules.system.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author：dcy
 * @Description: 角色分页条件查询使用
 * @Date: 2020/10/23 13:52
 */
@Getter
@Setter
@ApiModel(value = "RoleSearchInputDTO", description = "角色分页条件查询使用")
public class RoleSearchInputDTO {

    @ApiModelProperty(value = "角色名")
    private String roleName;

}

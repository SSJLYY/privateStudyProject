package com.dcy.modules.system.dto.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/10/23 9:18
 */
@Getter
@Setter
@ApiModel(value="RoleListOutputDTO", description="角色分页表格")
public class RoleListOutputDTO {

    @ApiModelProperty(value = "角色id")
    private String id;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色权限字符串")
    private String roleKey;

    @ApiModelProperty(value = "角色状态（0、正常；1、禁用）")
    private String roleStatus;

    @ApiModelProperty(value = "数据范围（1、全部数据权限；2、自定数据权限；3、本部门数据权限；4、本部门及以下数据权限）")
    private String dataScope;
}

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
 * @Date: 2020/10/23 14:15
 */
@Getter
@Setter
@ApiModel(value = "RoleCreateInputDTO", description = "创建角色使用")
public class RoleCreateInputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色权限字符串")
    private String roleKey;

    @ApiModelProperty(value = "角色状态（0、正常；1、禁用）")
    private String roleStatus;

    @ApiModelProperty(value = "数据范围（1、全部数据权限；2、自定数据权限；3、本部门数据权限；4、本部门及以下数据权限）")
    private String dataScope;

    @ApiModelProperty(value = "数据权限自定义部门ids")
    private List<String> deptIds;
}

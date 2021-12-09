package com.dcy.modules.system.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author dcy
 * @since 2021-03-16
 */
@Getter
@Setter
@ApiModel(value = "DeptUpdateInputDTO", description = "部门修改表单")
public class DeptUpdateInputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部门id")
    @NotBlank(message = "部门id不能为空")
    private String id;

    @ApiModelProperty(value = "父部门id")
    private String parentId;

    @ApiModelProperty(value = "祖级列表")
    private String ancestors;

    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty(value = "显示顺序")
    private BigDecimal deptSort;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "部门状态（0、正常；1、停用）")
    private String deptStatus;


}

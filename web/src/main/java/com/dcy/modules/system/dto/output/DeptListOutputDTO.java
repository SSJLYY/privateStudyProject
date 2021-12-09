package com.dcy.modules.system.dto.output;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/3/16 13:17
 */
@Getter
@Setter
@ApiModel(value = "DeptListOutputDTO对象", description = "tree表格数据")
public class DeptListOutputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部门id")
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

    @ApiModelProperty(value = "子级数据")
    @JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue)
    private List<DeptListOutputDTO> children;
}

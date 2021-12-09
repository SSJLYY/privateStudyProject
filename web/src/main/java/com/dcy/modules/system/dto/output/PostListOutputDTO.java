package com.dcy.modules.system.dto.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 岗位表
 * </p>
 *
 * @author dcy
 * @since 2021-03-16
 */
@Getter
@Setter
@ApiModel(value="PostListOutputDTO对象", description="岗位表格数据")
public class PostListOutputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "岗位ID")
    private String id;

    @ApiModelProperty(value = "岗位编码")
    private String postCode;

    @ApiModelProperty(value = "岗位名称")
    private String postName;

    @ApiModelProperty(value = "显示顺序")
    private BigDecimal postSort;

    @ApiModelProperty(value = "岗位状态（0、正常；1、停用）")
    private String postStatus;


}

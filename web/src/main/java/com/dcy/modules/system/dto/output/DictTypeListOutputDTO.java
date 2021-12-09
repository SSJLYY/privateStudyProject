package com.dcy.modules.system.dto.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 字典类型表
 * </p>
 *
 * @author dcy
 * @since 2021-03-17
 */
@Getter
@Setter
@ApiModel(value = "DictTypeListOutputDTO对象", description = "字典表格使用")
public class DictTypeListOutputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "字典主键")
    private String id;

    @ApiModelProperty(value = "字典名称")
    private String dictName;

    @ApiModelProperty(value = "字典类型")
    private String dictType;

    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String dictStatus;


}

package com.dcy.modules.system.dto.input;

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
@ApiModel(value="DictTypeCreateInputDTO", description="字典类型添加使用")
public class DictTypeCreateInputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "字典名称")
    private String dictName;

    @ApiModelProperty(value = "字典类型")
    private String dictType;

    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String dictStatus;



}

package com.dcy.modules.system.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 字典数据表
 * </p>
 *
 * @author dcy
 * @since 2021-03-17
 */
@Getter
@Setter
@ApiModel(value = "DictDataSearchInputDTO", description = "字典数据查询表单")
public class DictDataSearchInputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "字典标签")
    private String dictLabel;

    @ApiModelProperty(value = "字典类型")
    private String dictType;

    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String dictStatus;


}

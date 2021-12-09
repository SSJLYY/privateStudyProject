package com.dcy.modules.system.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/10/23 15:07
 */
@Getter
@Setter
@ApiModel(value = "ConfigSearchInputDTO", description = "配置查询使用")
public class ConfigSearchInputDTO {

    @ApiModelProperty(value = "参数名称")
    private String configName;
}

package com.dcy.modules.system.dto.output;

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
@ApiModel(value = "ConfigListOutputDTO", description = "创建配置使用")
public class ConfigListOutputDTO {

    @ApiModelProperty(value = "参数id")
    private String id;

    @ApiModelProperty(value = "参数名称")
    private String configName;

    @ApiModelProperty(value = "参数键名")
    private String configKey;

    @ApiModelProperty(value = "参数键值")
    private String configValue;

    @ApiModelProperty(value = "系统内置")
    private String configType;
}

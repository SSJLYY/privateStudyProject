package com.dcy.modules.system.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/10/23 15:07
 */
@Getter
@Setter
@ApiModel(value = "ConfigUpdateInputDTO", description = "修改配置使用")
public class ConfigUpdateInputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "参数id")
    @NotBlank(message = "参数id不能为空")
    private String id;

    @ApiModelProperty(value = "参数名称")
    private String configName;

    @ApiModelProperty(value = "参数键名")
    private String configKey;

    @ApiModelProperty(value = "参数键值")
    private String configValue;

    @ApiModelProperty(value = "系统内置")
    private Integer configType;
}

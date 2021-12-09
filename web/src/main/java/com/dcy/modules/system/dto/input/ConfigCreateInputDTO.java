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
@ApiModel(value = "ConfigCreateInputDTO", description = "创建配置使用")
public class ConfigCreateInputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "参数名称")
    @NotBlank(message = "参数名称不能为空")
    private String configName;

    @ApiModelProperty(value = "参数键名")
    @NotBlank(message = "参数键名不能为空")
    private String configKey;

    @ApiModelProperty(value = "参数键值")
    @NotBlank(message = "参数键值不能为空")
    private String configValue;

    @ApiModelProperty(value = "系统内置")
    private Integer configType;
}

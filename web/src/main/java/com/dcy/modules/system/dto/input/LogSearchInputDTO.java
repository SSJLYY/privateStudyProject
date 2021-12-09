package com.dcy.modules.system.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author dcy
 * @since 2021-01-06
 */
@Getter
@Setter
@ApiModel(value = "LogSearchInputDTO", description = "日志查询条件")
public class LogSearchInputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "操作人员")
    private String operName;

    @ApiModelProperty(value = "请求地址")
    private String url;

    @ApiModelProperty(value = "ip地址")
    private String ip;

    @ApiModelProperty(value = "业务模块名称")
    private String businessName;

    @ApiModelProperty(value = "操作状态（0正常 1异常）")
    private String logStatus;

}

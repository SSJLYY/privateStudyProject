package com.dcy.modules.system.dto.output;

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
@ApiModel(value="LogListOutputDTO", description="日志表格查询结果")
public class LogListOutputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "操作人员")
    private String operName;

    @ApiModelProperty(value = "请求参数")
    private String operParam;

    @ApiModelProperty(value = "请求地址")
    private String url;

    @ApiModelProperty(value = "ip地址")
    private String ip;

    @ApiModelProperty(value = "业务模块名称")
    private String businessName;

    @ApiModelProperty(value = "操作状态（0正常 1异常）")
    private String logStatus;

    @ApiModelProperty(value = "方法名")
    private String method;

    @ApiModelProperty(value = "执行时间(毫秒数)")
    private Long exeTime;

    @ApiModelProperty(value = "返回结果")
    private String result;

    @ApiModelProperty(value = "错误信息")
    private String error;


}

package com.dcy.quartz.dto.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/4/3 14:51
 */
@Getter
@Setter
@ApiModel(value = "JobLogListOutputDTO", description = "定时任务调度日志列表")
public class JobLogListOutputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务日志ID")
    private String id;

    @ApiModelProperty(value = "任务名称")
    private String jobName;

    @ApiModelProperty(value = "任务组名")
    private String jobGroup;

    @ApiModelProperty(value = "调用目标字符串")
    private String invokeTarget;

    @ApiModelProperty(value = "日志信息")
    private String jobMessage;

    @ApiModelProperty(value = "执行状态（0正常 1失败）")
    private String jobStatus;

    @ApiModelProperty(value = "异常信息")
    private String exceptionInfo;

}

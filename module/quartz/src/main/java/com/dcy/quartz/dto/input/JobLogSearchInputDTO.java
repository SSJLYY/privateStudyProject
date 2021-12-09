package com.dcy.quartz.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/4/3 15:08
 */
@Getter
@Setter
@ApiModel(value = "JobLogInputDTO对象", description = "定时任务调度日志查询")
public class JobLogSearchInputDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务名称")
    private String jobName;

    @ApiModelProperty(value = "任务组名")
    private String jobGroup;

    @ApiModelProperty(value = "执行状态（0正常 1失败）")
    private String jobStatus;

    @ApiModelProperty(value = "执行开始时间")
    private String beginDate;

    @ApiModelProperty(value = "执行结束时间")
    private String endDate;
}
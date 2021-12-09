package com.dcy.quartz.dto.input;

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
@ApiModel(value = "JobSearchInputDTO", description = "定时任务调度查询条件")
public class JobSearchInputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务名称")
    private String jobName;

    @ApiModelProperty(value = "任务组名")
    private String jobGroup;

    @ApiModelProperty(value = "状态（0正常 1暂停）")
    private String jobStatus;

}

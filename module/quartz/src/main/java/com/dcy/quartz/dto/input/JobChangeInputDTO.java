package com.dcy.quartz.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/4/3 14:51
 */
@Getter
@Setter
@ApiModel(value = "JobChangeInputDTO", description = "定时任务状态修改")
public class JobChangeInputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务ID")
    @NotBlank(message = "任务id不能为空")
    private String id;

    @ApiModelProperty(value = "任务组名")
    private String jobGroup;

    @ApiModelProperty(value = "状态（0正常 1暂停）")
    private String jobStatus;

}

package com.dcy.oa.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author dcy
 * @since 2021-06-11
 */
@Getter
@Setter
@ApiModel(value="LeaveMeSearchInputDTO", description="我的申请查询表单")
public class LeaveMeSearchInputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "开始时间")
    private Date beginDate;

    @ApiModelProperty(value = "结束时间")
    private Date endDate;

    @ApiModelProperty(value = "请假类型")
    private String type;

}

package com.dcy.oa.dto.output;

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
@ApiModel(value = "LeaveListOutputDTO", description = "列表")
public class LeaveListOutputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "流程实例id")
    private String processInstanceId;

    @ApiModelProperty(value = "请假申请时间")
    private Date leaveDate;

    @ApiModelProperty(value = "开始时间")
    private Date beginDate;

    @ApiModelProperty(value = "结束时间")
    private Date endDate;

    @ApiModelProperty(value = "请假类型")
    private String type;

    @ApiModelProperty(value = "请假事由")
    private String reason;

    @ApiModelProperty(value = "状态（1：未开始；2：进行中；3：已完成；4：驳回）")
    private String status;

}

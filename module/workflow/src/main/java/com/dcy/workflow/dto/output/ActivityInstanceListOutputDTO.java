package com.dcy.workflow.dto.output;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.task.api.history.HistoricTaskInstance;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/6/10 14:21
 */
@Getter
@Setter
@ToString
@ApiModel(value = "ActivityInstanceListOutputDTO", description = "流程运行节点详细")
public class ActivityInstanceListOutputDTO {

    @ApiModelProperty(value = "环节名称")
    private String activityName;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "任务历时")
    private String duration;

    @ApiModelProperty(value = "操作人")
    private String assignee;

    @ApiModelProperty(value = "审批状态")
    private String status;

    @ApiModelProperty(value = "审批意见")
    private String comment;

    public ActivityInstanceListOutputDTO() {
    }

    public ActivityInstanceListOutputDTO(ActivityInstance activityInstance) {
        this.activityName = activityInstance.getActivityName();
        this.startTime = DateUtil.formatDateTime(activityInstance.getStartTime());
        this.endTime = DateUtil.formatDateTime(activityInstance.getEndTime());
        this.duration = DateUtil.formatBetween(activityInstance.getStartTime(), activityInstance.getEndTime(), BetweenFormatter.Level.SECOND);
    }

    public ActivityInstanceListOutputDTO(HistoricTaskInstance historicTaskInstance) {
        this.activityName = historicTaskInstance.getName();
        this.startTime = DateUtil.formatDateTime(historicTaskInstance.getCreateTime());
        this.endTime = DateUtil.formatDateTime(historicTaskInstance.getEndTime());
        this.duration = DateUtil.formatBetween(historicTaskInstance.getCreateTime(), historicTaskInstance.getEndTime(), BetweenFormatter.Level.SECOND);
    }
}

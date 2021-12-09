package com.dcy.quartz.enums;

import com.dcy.common.api.IErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ScheduleApiErrorCode implements IErrorCode {
    CRON_EXPRESSION_ERROR(3001, "cron表达式不正确"),
    SCHEDULER_EXPRESSION_ERROR(3002, "调度异常"),
    ;

    private final Integer code;
    private final String msg;

}

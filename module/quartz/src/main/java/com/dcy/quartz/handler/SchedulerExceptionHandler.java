package com.dcy.quartz.handler;

import com.dcy.common.exception.TaskException;
import com.dcy.common.model.R;
import com.dcy.quartz.enums.ScheduleApiErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/4/8 9:31
 */
@RestControllerAdvice
@Slf4j
public class SchedulerExceptionHandler {

    /**
     * 调度异常
     *
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(SchedulerException.class)
    public R<String> schedulerExceptionHandler(SchedulerException exception) {
        log.error("schedulerException [{}]", exception.toString());
        exception.printStackTrace();
        return R.error(ScheduleApiErrorCode.SCHEDULER_EXPRESSION_ERROR);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(TaskException.class)
    public R<String> taskExceptionHandler(TaskException exception) {
        log.error("taskExceptionHandler [{}]", exception.toString());
        exception.printStackTrace();
        return R.error(ScheduleApiErrorCode.SCHEDULER_EXPRESSION_ERROR);
    }
}

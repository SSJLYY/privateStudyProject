package com.dcy.quartz.task;

import com.dcy.common.constant.ScheduleConstants;
import com.dcy.quartz.model.Job;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度测试
 *
 * @author ruoyi
 */
@Slf4j
@Component("ryTask")
public class RyTask {

    @Autowired
    private Scheduler scheduler;

    /**
     * 调度任务有参方法（多参数）
     *
     * @param s
     * @param b
     * @param l
     * @param d
     * @param i
     */
    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        log.info("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i);
    }


    /**
     * 获取执行中job
     * @param jobName
     */
    public void curScheduler(String jobName) {
        try {
            scheduler.getCurrentlyExecutingJobs().forEach(jobExecutionContext -> {
                final JobDataMap jobDataMap = jobExecutionContext.getTrigger().getJobDataMap();
                final Job job = (Job) jobDataMap.get(ScheduleConstants.TASK_PROPERTIES);
                if (jobName.equals(job.getJobName())) {
                    // 业务处理
                }
            });
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        log.info("执行有参方法 {}", jobName);
    }

    /**
     * 调度任务有参方法
     *
     * @param params
     */
    public void ryParams(String params) {
        log.info("执行有参方法 {}", params);
    }

    /**
     * 调度任务无参方法
     */
    public void ryNoParams() {
        log.info("执行无参方法");
    }
}

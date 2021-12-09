package com.dcy.quartz.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dcy.common.constant.ScheduleConstants;
import com.dcy.quartz.enums.JobLogStatusEnum;
import com.dcy.quartz.model.JobLog;
import com.dcy.quartz.service.JobLogService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * 抽象quartz调用
 *
 * @author ruoyi
 */
@Slf4j
public abstract class AbstractQuartzJob implements Job {

    /**
     * 线程本地变量
     */
    private static ThreadLocal<Date> threadLocal = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        com.dcy.quartz.model.Job job = new com.dcy.quartz.model.Job();
        BeanUtil.copyProperties(context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES), job);
        try {
            before(context, job);
            doExecute(context, job);
            after(context, job, null);
        } catch (Exception e) {
            log.error("任务执行异常  - ：", e);
            after(context, job, e);
        }
    }

    /**
     * 执行前
     *
     * @param context 工作执行上下文对象
     * @param sysJob  系统计划任务
     */
    protected void before(JobExecutionContext context, com.dcy.quartz.model.Job sysJob) {
        threadLocal.set(new Date());
    }

    /**
     * 执行后
     *
     * @param context 工作执行上下文对象
     * @param sysJob  系统计划任务
     */
    protected void after(JobExecutionContext context, com.dcy.quartz.model.Job sysJob, Exception e) {
        Date startTime = threadLocal.get();
        threadLocal.remove();

        final JobLog jobLog = new JobLog();
        jobLog.setJobName(sysJob.getJobName());
        jobLog.setJobGroup(sysJob.getJobGroup());
        jobLog.setInvokeTarget(sysJob.getInvokeTarget());
        long runMs = startTime.getTime() - System.currentTimeMillis();
        jobLog.setJobMessage(jobLog.getJobName() + " 总共耗时：" + runMs + "毫秒");
        if (e != null) {
            jobLog.setJobStatus(JobLogStatusEnum.FAIL.code);
            String errorMsg = ExceptionUtil.getMessage(e);
            jobLog.setExceptionInfo(errorMsg);
        } else {
            jobLog.setJobStatus(JobLogStatusEnum.NORMAL.code);
        }

        // 写入数据库当中
        SpringUtil.getBean(JobLogService.class).save(jobLog);
    }

    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param sysJob  系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, com.dcy.quartz.model.Job sysJob) throws Exception;
}

package com.dcy.quartz.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dcy.common.constant.ScheduleConstants;
import com.dcy.common.exception.TaskException;
import com.dcy.common.model.PageModel;
import com.dcy.db.base.service.BaseService;
import com.dcy.quartz.mapper.JobMapper;
import com.dcy.quartz.model.Job;
import com.dcy.quartz.util.ScheduleUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 定时任务调度表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2021-04-03
 */
@Slf4j
@Service
public class JobService extends BaseService<JobMapper, Job> {

    @Autowired
    private Scheduler scheduler;

    /**
     * 运行一次任务
     *
     * @param jobId
     * @throws SchedulerException
     */
    public void run(String jobId) throws SchedulerException {
        Job tmpObj = getById(jobId);
        // 参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, tmpObj);
        scheduler.triggerJob(ScheduleUtils.getJobKey(jobId, tmpObj.getJobGroup()), dataMap);
    }

    /**
     * 删除job
     *
     * @param id
     * @throws SchedulerException
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobById(String id) throws SchedulerException {
        Job job = getById(id);
        deleteJob(job);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @throws SchedulerException
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobByIds(List<String> ids) throws SchedulerException {
        for (String jobId : ids) {
            Job job = getById(jobId);
            deleteJob(job);
        }
    }

    private void deleteJob(Job job) throws SchedulerException {
        String jobId = job.getId();
        String jobGroup = job.getJobGroup();
        if (removeById(jobId)) {
            scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
    }

    public IPage<Job> pageListByEntity(Job job, PageModel pageModel) {
        LambdaQueryWrapper<Job> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(job.getJobName()), Job::getJobName, job.getJobName());
        queryWrapper.like(StrUtil.isNotBlank(job.getJobGroup()), Job::getJobGroup, job.getJobGroup());
        queryWrapper.eq(StrUtil.isNotBlank(job.getJobStatus()), Job::getJobStatus, job.getJobStatus());
        return super.page(pageModel, queryWrapper);
    }

    /**
     * 切换状态
     *
     * @param job
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean changeStatus(Job job) throws SchedulerException {
        if (ScheduleConstants.Status.NORMAL.getValue().equals(job.getJobStatus())) {
            resumeJob(job);
        } else if (ScheduleConstants.Status.PAUSE.getValue().equals(job.getJobStatus())) {
            pauseJob(job);
        }
        return true;
    }

    /**
     * 恢复任务
     *
     * @param job
     * @return
     * @throws SchedulerException
     */
    private void resumeJob(Job job) throws SchedulerException {
        String jobId = job.getId();
        String jobGroup = job.getJobGroup();
        job.setJobStatus(ScheduleConstants.Status.NORMAL.getValue());
        if (updateById(job)) {
            scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
    }


    /**
     * 暂停任务
     *
     * @param job
     * @return
     * @throws SchedulerException
     */
    private void pauseJob(Job job) throws SchedulerException {
        String jobId = job.getId();
        String jobGroup = job.getJobGroup();
        job.setJobStatus(ScheduleConstants.Status.PAUSE.getValue());
        if (updateById(job)) {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
    }

    /**
     * 添加job
     *
     * @param job
     * @return
     * @throws SchedulerException
     */
    public Boolean saveJob(Job job) throws SchedulerException {
        if (save(job)) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
        return true;
    }

    /**
     * 修改job
     *
     * @param job
     * @return
     * @throws SchedulerException
     */
    public Boolean updateJob(Job job) throws SchedulerException {
        if (updateById(job)) {
            updateSchedulerJob(job, job.getJobGroup());
        }
        return null;
    }

    /**
     * 更新任务
     *
     * @param job      任务对象
     * @param jobGroup 任务组名
     */
    private void updateSchedulerJob(Job job, String jobGroup) throws SchedulerException, TaskException {
        String jobId = job.getId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, job);
    }
}

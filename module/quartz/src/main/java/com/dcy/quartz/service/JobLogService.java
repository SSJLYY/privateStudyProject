package com.dcy.quartz.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dcy.common.model.PageModel;
import com.dcy.db.base.service.BaseService;
import com.dcy.quartz.mapper.JobLogMapper;
import com.dcy.quartz.model.JobLog;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务调度日志表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2021-04-03
 */
@Service
public class JobLogService extends BaseService<JobLogMapper, JobLog> {

    public IPage<JobLog> pageListByEntity(JobLog jobLog, PageModel pageModel) {
        LambdaQueryWrapper<JobLog> queryWrapper = Wrappers.<JobLog>lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(jobLog.getJobName()), JobLog::getJobName, jobLog.getJobName());
        queryWrapper.like(StrUtil.isNotBlank(jobLog.getJobGroup()), JobLog::getJobGroup, jobLog.getJobGroup());
        queryWrapper.eq(StrUtil.isNotBlank(jobLog.getJobStatus()), JobLog::getJobStatus, jobLog.getJobStatus());
        queryWrapper.between(StrUtil.isNotBlank(jobLog.getBeginDate()) && StrUtil.isNotBlank(jobLog.getEndDate()), JobLog::getCreateDate, jobLog.getBeginDate(), jobLog.getEndDate());
        return super.page(pageModel, queryWrapper);
    }

    public void removeJobLog() {
        baseMapper.deleteJobLog();
    }
}

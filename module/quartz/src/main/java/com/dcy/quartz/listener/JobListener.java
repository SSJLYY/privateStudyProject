package com.dcy.quartz.listener;

import com.dcy.quartz.mapper.JobMapper;
import com.dcy.quartz.model.Job;
import com.dcy.quartz.util.ScheduleUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author：dcy
 * @Description: 项目启动时，初始化定时器。主要是防止手动修改数据库导致未同步到定时任务处理（注：不能手动修改数据库ID和任务组名，否则会导致脏数据）
 * @Date: 2021/5/13 8:14
 */
@Slf4j
@Component
public class JobListener {

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private JobMapper jobMapper;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            scheduler.clear();
            List<Job> jobList = jobMapper.selectList(null);
            for (Job job : jobList) {
                ScheduleUtils.createScheduleJob(scheduler, job);
            }
            log.info("Scheduler 初始化定时器成功 ");
        } catch (SchedulerException e) {
            log.error("Scheduler 初始化定时器失败 {}", e.getMessage());
        }
    }

}

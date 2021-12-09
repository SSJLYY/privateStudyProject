package com.dcy.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dcy.quartz.model.JobLog;

/**
 * <p>
 * 定时任务调度日志表 Mapper 接口
 * </p>
 *
 * @author dcy
 * @since 2021-04-03
 */
public interface JobLogMapper extends BaseMapper<JobLog> {

    /**
     * 清空数据
     */
    void deleteJobLog();
}

package com.dcy.system.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dcy.common.model.PageModel;
import com.dcy.db.base.service.BaseService;
import com.dcy.system.mapper.LogMapper;
import com.dcy.system.model.Log;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2021-01-06
 */
@Service
public class LogService extends BaseService<LogMapper, Log> {

    /**
     * 根据对象查询
     *
     * @param log
     * @return
     */
    public IPage<Log> pageListByEntity(Log log, PageModel pageModel) {
        LambdaQueryWrapper<Log> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(log.getOperName()), Log::getOperName, log.getOperName());
        queryWrapper.like(StrUtil.isNotBlank(log.getUrl()), Log::getUrl, log.getUrl());
        queryWrapper.like(StrUtil.isNotBlank(log.getIp()), Log::getIp, log.getIp());
        queryWrapper.like(StrUtil.isNotBlank(log.getBusinessName()), Log::getBusinessName, log.getBusinessName());
        queryWrapper.eq(StrUtil.isNotBlank(log.getLogStatus()), Log::getLogStatus, log.getLogStatus());
        queryWrapper.orderByDesc(Log::getCreateDate);
        return super.page(pageModel, queryWrapper);
    }

    /**
     * 异步调用保存
     *
     * @param log
     */
    @Async
    public void saveLog(Log log) {
        save(log);
    }
}

package com.dcy.workflow.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dcy.common.constant.Constant;
import com.dcy.common.constant.TaskConstants;
import com.dcy.common.service.ActCompleteTaskService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

import java.util.Arrays;
import java.util.List;

/**
 * @Author：dcy
 * @Description: 驳回任务监听器（用于每个任务节点）
 * @Date: 2021/6/15 9:25
 */
@Slf4j
public class RejectTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        // ====================获取基本信息========================
        final String status = delegateTask.getVariable(TaskConstants.TASK_STATUS, String.class);
        if (StrUtil.isNotBlank(status) && TaskConstants.TASK_REJECT.equals(status)) {
            final String businessKey = delegateTask.getVariable(TaskConstants.TASK_BUSINESS_KEY, String.class);
            log.info("businessKey:{}", businessKey);
            final List<String> list = StrUtil.split(businessKey, ":");
            // 获取表名
            final String tableName = CollUtil.getFirst(list);
            // 获取主键id
            final String id = CollUtil.getLast(list);
            if (StrUtil.isNotBlank(tableName) && StrUtil.isNotBlank(id)) {
                final ActCompleteTaskService actCompleteTaskService = SpringUtil.getBean(tableName + "Service", ActCompleteTaskService.class);
                if (actCompleteTaskService != null) {
                    // 驳回状态
                    actCompleteTaskService.updateRejectStatusById(id);
                }
            }
        }
    }

}
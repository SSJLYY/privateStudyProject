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
 * @Description: 完成用户任务监听器（用于最后一个节点）
 * @Date: 2021/6/8 8:28
 */
@Slf4j
public class CompleteTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        // ====================获取基本信息========================
        final String businessKey = delegateTask.getVariable(TaskConstants.TASK_BUSINESS_KEY, String.class);
        final String status = delegateTask.getVariable(TaskConstants.TASK_STATUS, String.class);
        log.info("businessKey:{}", businessKey);
        final List<String> list = StrUtil.split(businessKey, ":");
        // 获取表名
        final String tableName = CollUtil.getFirst(list);
        // 获取主键id
        final String id = CollUtil.getLast(list);
        if (StrUtil.isNotBlank(tableName) && StrUtil.isNotBlank(id)) {
            final ActCompleteTaskService actCompleteTaskService = SpringUtil.getBean(tableName + "Service", ActCompleteTaskService.class);
            if (actCompleteTaskService != null && StrUtil.isNotBlank(status)) {
                switch (status) {
                    case TaskConstants.TASK_SUCCESS:
                        // 通过状态
                        actCompleteTaskService.updateCompleteStatusById(id);
                        break;
                    case TaskConstants.TASK_REJECT:
                        // 驳回状态
                        actCompleteTaskService.updateRejectStatusById(id);
                        break;
                    default:
                }
            }

        }

    }

}

package com.dcy.oa.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dcy.common.model.flowable.ActProcessInstance;
import com.dcy.common.service.ActCompleteTaskService;
import com.dcy.common.service.ActFlowableTaskService;
import com.dcy.common.model.PageModel;
import com.dcy.db.base.service.BaseService;
import com.dcy.oa.enums.LeaveStatusEnum;
import com.dcy.oa.mapper.LeaveMapper;
import com.dcy.oa.model.Leave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 请假服务实现类
 * </p>
 *
 * @author dcy
 * @since 2021-06-11
 */
@Service(value = "leaveService")
public class LeaveService extends BaseService<LeaveMapper, Leave> implements ActCompleteTaskService {

    @Autowired
    private ActFlowableTaskService actFlowableTaskService;

    /**
     * 获取表格数据
     *
     * @param leave
     * @return
     */
    public IPage<Leave> pageListByEntity(Leave leave, PageModel pageModel) {
        LambdaQueryWrapper<Leave> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(StrUtil.isNotBlank(leave.getUserId()), Leave::getUserId, leave.getUserId());
        queryWrapper.like(StrUtil.isNotBlank(leave.getName()), Leave::getName, leave.getName());
        queryWrapper.eq(StrUtil.isNotBlank(leave.getType()), Leave::getType, leave.getType());
        queryWrapper.between(leave.getBeginDate() != null && leave.getEndDate() != null, Leave::getLeaveDate, leave.getBeginDate(), leave.getEndDate());
        return super.page(pageModel, queryWrapper);
    }

    /**
     * 保存请假申请
     *
     * @param leave
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveLeave(Leave leave, String userId) {
        // 获取一个新的id
        final String idStr = IdWorker.getIdStr();
        leave.setId(idStr);
        // 拼装 业务key
        String businessKey = StrUtil.builder()
                .append("leave:")
                .append(idStr).toString();
        // 启动流程 并且 完成首个任务
        final ActProcessInstance actProcessInstance = actFlowableTaskService.startProcessAndCompleteFirstTask("leave", businessKey, userId);
        // 业务表关联流程id
        leave.setProcessInstanceId(actProcessInstance.getProcessInstanceId());
        leave.setStatus(LeaveStatusEnum.HAVE_IN_HAND.code);
        return save(leave);
    }

    /**
     * 重新提交
     *
     * @param leave
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean reSubmitLeave(Leave leave, String userId) {
        final Leave leaveOne = getById(leave.getId());
        leave.setStatus(LeaveStatusEnum.HAVE_IN_HAND.code);
        final boolean success = updateById(leave);
        final String taskId = actFlowableTaskService.getTaskIdByProcessInstanceId(leaveOne.getProcessInstanceId(), leaveOne.getUserId());
        if (success) {
            actFlowableTaskService.complete(taskId, userId, "重新提交");
        }
        return success;
    }

    /**
     * 提交
     *
     * @param leave
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean submit(Leave leave, String userId) {
        // 拼装 业务key
        String businessKey = StrUtil.builder()
                .append("leave:")
                .append(leave.getId()).toString();
        // 启动流程 并且 完成首个任务
        final ActProcessInstance actProcessInstance = actFlowableTaskService.startProcessAndCompleteFirstTask("leave", businessKey, userId);
        // 业务表关联流程id
        leave.setProcessInstanceId(actProcessInstance.getProcessInstanceId());
        leave.setLeaveDate(new Date());
        leave.setStatus(LeaveStatusEnum.HAVE_IN_HAND.code);
        return updateById(leave);
    }

    @Override
    public void updateCompleteStatusById(String id) {
        Leave leave = new Leave();
        leave.setId(id);
        leave.setStatus(LeaveStatusEnum.COMPLETED.code);
        updateById(leave);
    }

    @Override
    public void updateRejectStatusById(String id) {
        Leave leave = new Leave();
        leave.setId(id);
        leave.setStatus(LeaveStatusEnum.REJECT.code);
        updateById(leave);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteLeave(String id) {
        final Leave leave = getById(id);
        if (StrUtil.isNotBlank(leave.getProcessInstanceId())) {
            actFlowableTaskService.deleteProcess(leave.getProcessInstanceId());
        }
        return removeById(id);
    }
}

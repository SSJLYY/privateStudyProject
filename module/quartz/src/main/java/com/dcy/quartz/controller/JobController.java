package com.dcy.quartz.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dcy.common.annotation.Log;
import com.dcy.common.model.R;
import com.dcy.db.base.controller.BaseController;
import com.dcy.common.model.PageModel;
import com.dcy.common.model.PageResult;
import com.dcy.quartz.dto.input.JobChangeInputDTO;
import com.dcy.quartz.dto.input.JobCreateInputDTO;
import com.dcy.quartz.dto.input.JobSearchInputDTO;
import com.dcy.quartz.dto.input.JobUpdateInputDTO;
import com.dcy.quartz.dto.output.JobListOutputDTO;
import com.dcy.quartz.dtomapper.MJobMapper;
import com.dcy.quartz.enums.ScheduleApiErrorCode;
import com.dcy.quartz.model.Job;
import com.dcy.quartz.service.JobService;
import com.dcy.quartz.util.CronUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 参数配置表 前端控制器
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/monitor/job")
@Api(value = "JobController", tags = {"调度任务信息操作接口"})
public class JobController extends BaseController<JobService, Job> {

    private final MJobMapper mJobMapper;

    @Log
    @ApiOperation(value = "调度任务分页查询")
    @GetMapping("/page")
    public R<PageResult<JobListOutputDTO>> pageList(JobSearchInputDTO jobSearchInputDTO, PageModel pageModel) {
        // 转换model
        Job job = mJobMapper.toJob(jobSearchInputDTO);
        // 获取源对象
        IPage<Job> pageListByListInputDTO = baseService.pageListByEntity(job, pageModel);
        // 转换新对象
        List<JobListOutputDTO> userInfoListOutputDTOS = mJobMapper.toList(pageListByListInputDTO.getRecords());
        // 返回业务分页数据
        return success(toPageDTO(pageListByListInputDTO, userInfoListOutputDTOS));
    }


    @ApiOperation(value = "立即执行一次")
    @ApiImplicitParam(name = "jobId", value = "任务id", dataType = "String", paramType = "query", required = true)
    @PostMapping("/run")
    public R<Boolean> run(@Validated @NotBlank(message = "任务id不能为空") @RequestParam String jobId) throws SchedulerException {
        baseService.run(jobId);
        return success(true);
    }

    @Log
    @ApiOperation(value = "添加")
    @PostMapping("/save")
    public R<Boolean> saveJob(@Validated @ApiParam @RequestBody JobCreateInputDTO jobCreateInputDTO) throws SchedulerException {
        if (!CronUtils.isValid(jobCreateInputDTO.getCronExpression())) {
            return R.error(ScheduleApiErrorCode.CRON_EXPRESSION_ERROR);
        }
        Job job = mJobMapper.toJob(jobCreateInputDTO);
        return success(baseService.saveJob(job));
    }

    @Log
    @ApiOperation(value = "修改")
    @PostMapping("/update")
    public R<Boolean> updateJob(@Validated @ApiParam @RequestBody JobUpdateInputDTO jobUpdateInputDTO) throws SchedulerException {
        if (!CronUtils.isValid(jobUpdateInputDTO.getCronExpression())) {
            return R.error(ScheduleApiErrorCode.CRON_EXPRESSION_ERROR);
        }
        Job job = mJobMapper.toJob(jobUpdateInputDTO);
        return success(baseService.updateJob(job));
    }

    @Log
    @ApiOperation(value = "切换状态")
    @PostMapping("/changeStatus")
    public R<Boolean> changeStatus(@Validated @ApiParam @RequestBody JobChangeInputDTO jobChangeInputDTO) throws SchedulerException {
        Job job = mJobMapper.toJob(jobChangeInputDTO);
        return success(baseService.changeStatus(job));
    }

    @Log
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "id", value = "任务id", dataType = "String", paramType = "query", required = true)
    @PostMapping("/delete")
    public R<Boolean> delete(@NotBlank(message = "任务id不能为空") @RequestParam String id) throws SchedulerException {
        baseService.deleteJobById(id);
        return success(true);
    }

    @Log
    @ApiOperation(value = "根据list删除")
    @PostMapping("/deleteBatch")
    public R<Boolean> deleteBatch(@NotEmpty(message = "集合不能为空") @ApiParam @RequestBody List<String> ids) throws SchedulerException {
        baseService.deleteJobByIds(ids);
        return success();
    }

    @ApiOperation(value = "校验cron表达式是否有效")
    @ApiImplicitParam(name = "cronExpression", value = "cron表达式", dataType = "String", paramType = "query", required = true)
    @PostMapping("/checkCronExpressionIsValid")
    public R<Boolean> checkCronExpressionIsValid(@NotBlank(message = "cron表达式为空") String cronExpression) {
        return success(CronUtils.isValid(cronExpression));
    }
}

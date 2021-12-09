package com.dcy.quartz.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dcy.common.annotation.Log;
import com.dcy.common.model.R;
import com.dcy.db.base.controller.BaseController;
import com.dcy.common.model.PageModel;
import com.dcy.common.model.PageResult;
import com.dcy.quartz.dto.input.JobLogSearchInputDTO;
import com.dcy.quartz.dto.output.JobLogListOutputDTO;
import com.dcy.quartz.dtomapper.MJobLogMapper;
import com.dcy.quartz.model.JobLog;
import com.dcy.quartz.service.JobLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 定时任务调度日志表 前端控制器
 * </p>
 *
 * @author dcy
 * @since 2021-04-03
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/monitor/jobLog")
@Api(value = "JobController", tags = {"调度日志操作接口"})
public class JobLogController extends BaseController<JobLogService, JobLog> {

    private final MJobLogMapper mJobLogMapper;

    @Log
    @ApiOperation(value = "调度任务分页查询")
    @GetMapping("/page")
    public R<PageResult<JobLogListOutputDTO>> pageList(JobLogSearchInputDTO jobLogSearchInputDTO, PageModel pageModel) {
        // 转换model
        JobLog jobLog = mJobLogMapper.toJobLog(jobLogSearchInputDTO);
        // 获取源对象
        IPage<JobLog> pageListByListInputDTO = baseService.pageListByEntity(jobLog,pageModel);
        // 转换新对象
        List<JobLogListOutputDTO> userInfoListOutputDTOS = mJobLogMapper.toList(pageListByListInputDTO.getRecords());
        // 返回业务分页数据
        return success(toPageDTO(pageListByListInputDTO, userInfoListOutputDTOS));
    }

    @Log
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "jobId", value = "任务id", dataType = "String", paramType = "query", required = true)
    @PostMapping("/delete")
    public R<Boolean> delete(@NotBlank(message = "任务id不能为空") @RequestParam String id) {
        return success(baseService.removeById(id));
    }

    @Log
    @ApiOperation(value = "根据list删除")
    @PostMapping("/deleteBatch")
    public R<Boolean> deleteBatch(@NotEmpty(message = "集合不能为空") @ApiParam @RequestBody List<String> ids) {
        return success(baseService.removeByIds(ids));
    }

    @Log
    @ApiOperation(value = "清空日志")
    @PostMapping("/clean")
    public R<Boolean> clean() {
        baseService.removeJobLog();
        return success(true);
    }
}

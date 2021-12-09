package com.dcy.oa.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dcy.common.annotation.Log;
import com.dcy.common.model.R;
import com.dcy.common.service.ActUserService;
import com.dcy.db.base.controller.BaseController;
import com.dcy.common.model.PageModel;
import com.dcy.common.model.PageResult;
import com.dcy.oa.dto.input.LeaveCreateInputDTO;
import com.dcy.oa.dto.input.LeaveMeSearchInputDTO;
import com.dcy.oa.dto.input.LeaveSearchInputDTO;
import com.dcy.oa.dto.input.LeaveUpdateInputDTO;
import com.dcy.oa.dto.output.LeaveListOutputDTO;
import com.dcy.oa.dtomapper.MLeaveMapper;
import com.dcy.oa.enums.LeaveStatusEnum;
import com.dcy.oa.model.Leave;
import com.dcy.oa.service.LeaveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 请假前端控制器
 * </p>
 *
 * @author dcy
 * @since 2021-06-11
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/oa/leave")
@Api(value = "LeaveController", tags = {"请假操作接口"})
public class LeaveController extends BaseController<LeaveService, Leave> {

    private final MLeaveMapper mLeaveMapper;
    private final ActUserService actUserService;

    @Log
    @ApiOperation(value = "请假分页查询")
    @GetMapping("/page")
    public R<PageResult<LeaveListOutputDTO>> pageList(LeaveSearchInputDTO leaveSearchInputDTO, PageModel pageModel) {
        // 转换model
        Leave leave = mLeaveMapper.toLeave(leaveSearchInputDTO);
        // 获取源对象
        IPage<Leave> pageListByListInputDTO = baseService.pageListByEntity(leave, pageModel);
        // 转换新对象
        List<LeaveListOutputDTO> leaveListOutputDTOS = mLeaveMapper.toList(pageListByListInputDTO.getRecords());
        // 返回业务分页数据
        return success(toPageDTO(pageListByListInputDTO, leaveListOutputDTOS));
    }

    @Log
    @ApiOperation(value = "我的申请")
    @GetMapping("/getMeLeavePageList")
    public R<PageResult<LeaveListOutputDTO>> getMeLeavePageList(LeaveMeSearchInputDTO leaveSearchInputDTO, PageModel pageModel) {
        // 转换model
        Leave leave = mLeaveMapper.toLeave(leaveSearchInputDTO);
        leave.setUserId(getUserId());
        // 获取源对象
        IPage<Leave> pageListByListInputDTO = baseService.pageListByEntity(leave, pageModel);
        // 转换新对象
        List<LeaveListOutputDTO> leaveListOutputDTOS = mLeaveMapper.toList(pageListByListInputDTO.getRecords());
        // 返回业务分页数据
        return success(toPageDTO(pageListByListInputDTO, leaveListOutputDTOS));
    }

    @Log
    @ApiOperation(value = "获取详细")
    @ApiImplicitParam(name = "id", value = "id", dataType = "String", paramType = "query", required = true)
    @GetMapping("/getLeaveById")
    public R<LeaveListOutputDTO> getLeaveById(@NotBlank(message = "id不能为空") String id) {
        final Leave leave = baseService.getById(id);
        return success(mLeaveMapper.toList(leave));
    }

    @Log
    @ApiOperation(value = "保存（不启动流程）")
    @PostMapping("/save")
    public R<Boolean> save(@Validated @ApiParam @RequestBody LeaveCreateInputDTO leaveCreateInputDTO) {
        Leave leave = mLeaveMapper.toLeave(leaveCreateInputDTO);
        final String userId = getUserId();
        leave.setUserId(userId);
        leave.setName(actUserService.getNickNameByUserId(userId));
        leave.setStatus(LeaveStatusEnum.NOT_STARTED.code);
        return success(baseService.save(leave));
    }

    @Log
    @ApiOperation(value = "提交（启动流程）")
    @ApiImplicitParam(name = "id", value = "id", dataType = "String", paramType = "query", required = true)
    @PostMapping("/submit")
    public R<Boolean> submit(@Validated @ApiParam @RequestBody LeaveUpdateInputDTO leaveUpdateInputDTO) {
        Leave leave = mLeaveMapper.toLeave(leaveUpdateInputDTO);
        return success(baseService.submit(leave, getUserId()));
    }

    @Log
    @ApiOperation(value = "创建并且提交（启动流程）")
    @PostMapping("/createAndSubmit")
    public R<Boolean> createAndSubmit(@Validated @ApiParam @RequestBody LeaveCreateInputDTO leaveCreateInputDTO) {
        Leave leave = mLeaveMapper.toLeave(leaveCreateInputDTO);
        final String userId = getUserId();
        leave.setUserId(userId);
        leave.setName(actUserService.getNickNameByUserId(userId));
        leave.setStatus(LeaveStatusEnum.NOT_STARTED.code);
        leave.setLeaveDate(new Date());
        return success(baseService.saveLeave(leave, getUserId()));
    }

    @Log
    @ApiOperation(value = "重新提交")
    @PostMapping(value = "/reSubmit")
    public R<Boolean> reSubmit(@Validated @ApiParam @RequestBody LeaveUpdateInputDTO leaveUpdateInputDTO) {
        Leave leave = mLeaveMapper.toLeave(leaveUpdateInputDTO);
        return success(baseService.reSubmitLeave(leave, getUserId()));
    }

    @Log
    @ApiOperation(value = "删除审批并且删除流程实例")
    @ApiImplicitParam(name = "id", value = "id", dataType = "String", paramType = "query", required = true)
    @PostMapping(value = "/delete")
    public R<Boolean> delete(@NotBlank(message = "id不能为空") @RequestParam String id) {
        return success(baseService.deleteLeave(id));
    }

}

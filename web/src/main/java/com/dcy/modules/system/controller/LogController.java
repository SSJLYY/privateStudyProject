package com.dcy.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dcy.common.model.R;
import com.dcy.db.base.controller.BaseController;
import com.dcy.common.model.PageModel;
import com.dcy.common.model.PageResult;
import com.dcy.modules.system.dto.input.LogSearchInputDTO;
import com.dcy.modules.system.dto.output.LogListOutputDTO;
import com.dcy.modules.system.dtomapper.MLogMapper;
import com.dcy.system.model.Log;
import com.dcy.system.service.LogService;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 日志表 前端控制器
 * </p>
 *
 * @author dcy
 * @since 2021-01-06
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/log")
@ApiSupport(order = 35)
@Api(value = "LogController", tags = {"日志接口"})
public class LogController extends BaseController<LogService, Log> {

    private final MLogMapper mLogMapper;

    @ApiOperation(value = "日志分页查询")
    @GetMapping("/page")
    public R<PageResult<LogListOutputDTO>> pageList(LogSearchInputDTO logSearchInputDTO, PageModel pageModel) {
        // 转换model
        Log log = mLogMapper.logSearchInputDTOToLog(logSearchInputDTO);
        // 获取源对象
        IPage<Log> pageListByListInputDTO = baseService.pageListByEntity(log, pageModel);
        // 转换新对象
        List<LogListOutputDTO> userInfoListOutputDTOS = mLogMapper.logsToLogListOutputDTOs(pageListByListInputDTO.getRecords());
        // 返回业务分页数据
        return success(toPageDTO(pageListByListInputDTO, userInfoListOutputDTOS));
    }
}

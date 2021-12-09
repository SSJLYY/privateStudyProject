package com.dcy.oa.dtomapper;

import com.dcy.oa.dto.input.LeaveCreateInputDTO;
import com.dcy.oa.dto.input.LeaveMeSearchInputDTO;
import com.dcy.oa.dto.input.LeaveSearchInputDTO;
import com.dcy.oa.dto.input.LeaveUpdateInputDTO;
import com.dcy.oa.dto.output.LeaveListOutputDTO;
import com.dcy.oa.model.Leave;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author：dcy
 * @Description: 转换类
 * @Date: 2021-06-11
 */
@Mapper(componentModel = "spring")
public interface MLeaveMapper {

    Leave toLeave(LeaveSearchInputDTO leaveSearchInputDTO);

    Leave toLeave(LeaveMeSearchInputDTO leaveMeSearchInputDTO);

    Leave toLeave(LeaveCreateInputDTO leaveCreateInputDTO);

    Leave toLeave(LeaveUpdateInputDTO leaveUpdateInputDTO);

    LeaveListOutputDTO toList(Leave leave);

    List<LeaveListOutputDTO> toList(List<Leave> leaves);
}
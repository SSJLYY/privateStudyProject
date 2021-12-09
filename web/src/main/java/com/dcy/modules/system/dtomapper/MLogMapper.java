package com.dcy.modules.system.dtomapper;

import com.dcy.modules.system.dto.input.*;
import com.dcy.modules.system.dto.output.LogListOutputDTO;
import com.dcy.modules.system.dto.output.LoginOutputDTO;
import com.dcy.modules.system.dto.output.UserInfoListOutputDTO;
import com.dcy.system.model.Log;
import com.dcy.system.model.UserInfo;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/10/23 9:28
 */
@Mapper(componentModel = "spring")
public interface MLogMapper {

    Log logSearchInputDTOToLog(LogSearchInputDTO logSearchInputDTO);

    LogListOutputDTO logToLogListOutputDTO(Log log);

    List<LogListOutputDTO> logsToLogListOutputDTOs(List<Log> logs);


}

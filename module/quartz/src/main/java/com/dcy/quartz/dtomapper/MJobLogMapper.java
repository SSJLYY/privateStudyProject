package com.dcy.quartz.dtomapper;

import com.dcy.quartz.dto.input.JobLogSearchInputDTO;
import com.dcy.quartz.dto.output.JobLogListOutputDTO;
import com.dcy.quartz.model.JobLog;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/12/17 8:47
 */
@Mapper(componentModel = "spring")
public interface MJobLogMapper {

    JobLog toJobLog(JobLogSearchInputDTO jobLogSearchInputDTO);

    JobLogListOutputDTO toList(JobLog jobLog);

    List<JobLogListOutputDTO> toList(List<JobLog> jobLogs);
}


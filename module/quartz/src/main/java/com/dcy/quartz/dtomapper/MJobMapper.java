package com.dcy.quartz.dtomapper;

import com.dcy.quartz.dto.input.JobChangeInputDTO;
import com.dcy.quartz.dto.input.JobCreateInputDTO;
import com.dcy.quartz.dto.input.JobSearchInputDTO;
import com.dcy.quartz.dto.input.JobUpdateInputDTO;
import com.dcy.quartz.dto.output.JobListOutputDTO;
import com.dcy.quartz.model.Job;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/12/17 8:47
 */
@Mapper(componentModel = "spring")
public interface MJobMapper {

    Job toJob(JobSearchInputDTO jobSearchInputDTO);

    Job toJob(JobCreateInputDTO jobCreateInputDTO);

    Job toJob(JobUpdateInputDTO jobUpdateInputDTO);

    Job toJob(JobChangeInputDTO jobChangeInputDTO);

    JobListOutputDTO toList(Job job);

    List<JobListOutputDTO> toList(List<Job> jobs);
}


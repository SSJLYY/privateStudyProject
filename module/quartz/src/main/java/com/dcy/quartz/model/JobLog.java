package com.dcy.quartz.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 定时任务调度日志表
 * </p>
 *
 * @author dcy
 * @since 2021-04-03
 */
@Data
@Accessors(chain = true)
@TableName("sys_job_log")
public class JobLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务日志ID
     */
    private String id;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务组名
     */
    private String jobGroup;

    /**
     * 调用目标字符串
     */
    private String invokeTarget;

    /**
     * 日志信息
     */
    private String jobMessage;

    /**
     * 执行状态（0正常 1失败）
     *
     * @see com.dcy.quartz.enums.JobLogStatusEnum
     */
    private String jobStatus;

    /**
     * 异常信息
     */
    private String exceptionInfo;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;

    /**
     * 执行开始时间
     */
    @TableField(exist = false)
    private String beginDate;

    /**
     * 执行结束时间
     */
    @TableField(exist = false)
    private String endDate;

    public static final String ID = "id";

    public static final String JOB_NAME = "job_name";

    public static final String JOB_GROUP = "job_group";

    public static final String INVOKE_TARGET = "invoke_target";

    public static final String JOB_MESSAGE = "job_message";

    public static final String JOB_STATUS = "job_status";

    public static final String EXCEPTION_INFO = "exception_info";

}

package com.dcy.oa.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 请假表
 * </p>
 *
 * @author dcy
 * @since 2021-06-11
 */
@Data
@Accessors(chain = true)
@TableName("oa_leave")
public class Leave implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 申请人
     */
    private String userId;

    /**
     * 申请人姓名
     */
    private String name;

    /**
     * 请假申请时间
     */
    private Date leaveDate;

    /**
     * 开始时间
     */
    private Date beginDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 请假类型
     */
    private String type;

    /**
     * 请假事由
     */
    private String reason;

    /**
     * 状态（1：未开始；2：进行中；3：已完成；4：已驳回）
     *
     * @see com.dcy.oa.enums.LeaveStatusEnum
     */
    private String status;


    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String BEGIN_DATE = "begin_date";

    public static final String END_DATE = "end_date";

    public static final String TYPE = "type";

    public static final String REASON = "reason";

    public static final String STATUS = "status";

}

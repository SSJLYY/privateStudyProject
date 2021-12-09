package com.dcy.system.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author dcy
 * @since 2021-01-06
 */
@Data
@Accessors(chain = true)
@TableName("sys_log")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 操作人员
     */
    private String operName;

    /**
     * 请求参数
     */
    private String operParam;

    /**
     * 请求地址
     */
    private String url;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 业务模块名称
     */
    private String businessName;

    /**
     * 方法名
     */
    private String method;

    /**
     * 返回结果
     */
    private String result;

    /**
     * 操作状态（0正常 1异常）
     *
     * @see com.dcy.system.enums.LogStatusEnum
     */
    private String logStatus;

    /**
     * 错误信息
     */
    private String error;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;
}

package com.dcy.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dcy.common.model.TreeModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author dcy
 * @since 2021-03-16
 */
@Data
@Accessors(chain = true)
@TableName("sys_dept")
public class Dept implements TreeModel<Dept>, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门id
     */
    private String id;

    /**
     * 父部门id
     */
    private String parentId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 显示顺序
     */
    private BigDecimal deptSort;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 部门状态（0、正常；1、停用）
     */
    private String deptStatus;

    @TableField(exist = false)
    private List<Dept> children;


    public static final String ID = "id";

    public static final String PARENT_ID = "parent_id";

    public static final String ANCESTORS = "ancestors";

    public static final String NAME = "name";

    public static final String DEPT_SORT = "dept_sort";

    public static final String PHONE = "phone";

    public static final String EMAIL = "email";

    public static final String DEPT_STATUS = "dept_status";
}

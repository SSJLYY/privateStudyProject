package com.dcy.system.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 岗位表
 * </p>
 *
 * @author dcy
 * @since 2021-03-16
 */
@Data
@Accessors(chain = true)
@TableName("sys_post")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 岗位ID
     */
    private String id;

    /**
     * 岗位编码
     */
    private String postCode;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 显示顺序
     */
    private BigDecimal postSort;

    /**
     * 岗位状态（0、正常；1、停用）
     */
    private String postStatus;


    public static final String ID = "id";

    public static final String POST_CODE = "post_code";

    public static final String POST_NAME = "post_name";

    public static final String POST_SORT = "post_sort";

    public static final String POST_STATUS = "post_status";

}

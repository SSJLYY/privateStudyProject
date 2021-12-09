package com.dcy.db.base.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author：dcy
 * @Description: 公共实体类
 * @Date: 2019/9/6 13:55
 */
@Getter
@Setter
public class BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(hidden = true)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(hidden = true)
    private Date createDate;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(hidden = true)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(hidden = true)
    private Date updateDate;

    /**
     * 删除标识（0：正常；1：已删除）
     *
     * @see com.dcy.db.base.enums.BaseModelDelFlagEnum
     */
    @TableLogic
    @ApiModelProperty(hidden = true)
    private String delFlag;

    /**
     * 备注
     */
    private String remark;


    public static final String CREATE_BY = "create_by";

    public static final String CREATE_DATE = "create_date";

    public static final String UPDATE_BY = "update_by";

    public static final String UPDATE_DATE = "update_date";

    public static final String DEL_FLAG = "del_flag";

    public static final String REMARK = "remark";

}

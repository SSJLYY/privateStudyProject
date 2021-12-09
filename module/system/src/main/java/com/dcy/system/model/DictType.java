package com.dcy.system.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 字典类型表
 * </p>
 *
 * @author dcy
 * @since 2021-03-17
 */
@Data
@Accessors(chain = true)
@TableName("sys_dict_type")
public class DictType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典主键
     */
    private String id;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 状态（0正常 1停用）
     */
    private String dictStatus;


    public static final String ID = "id";

    public static final String DICT_NAME = "dict_name";

    public static final String DICT_TYPE = "dict_type";

    public static final String DICT_STATUS = "dict_status";

    public static final String TREE_FLAG = "tree_flag";

}

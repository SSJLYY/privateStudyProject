package com.dcy.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dcy.common.model.TreeModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 字典数据表
 * </p>
 *
 * @author dcy
 * @since 2021-03-17
 */
@Data
@Accessors(chain = true)
@TableName("sys_dict_data")
public class DictData implements TreeModel<DictData>, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典id
     */
    private String id;

    /**
     * 父级id
     */
    private String parentId;

    /**
     * 字典标签
     */
    private String dictLabel;

    /**
     * 字典键值
     */
    private String dictValue;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 字典排序
     */
    private Integer dictSort;

    /**
     * 样式属性（其他样式扩展）
     */
    private String cssClass;

    /**
     * 表格回显样式
     */
    private String listClass;

    /**
     * 状态（0正常 1停用）
     *
     * @see com.dcy.system.enums.DictDataStatusEnum
     */
    private String dictStatus;

    /**
     * 子级数据
     */
    @TableField(exist = false)
    private List<DictData> children;

    public static final String ID = "id";

    public static final String PARENT_ID = "parent_id";

    public static final String DICT_LABEL = "dict_label";

    public static final String DICT_VALUE = "dict_value";

    public static final String DICT_TYPE = "dict_type";

    public static final String DICT_SORT = "dict_sort";

    public static final String CSS_CLASS = "css_class";

    public static final String LIST_CLASS = "list_class";

    public static final String DICT_STATUS = "dict_status";

}

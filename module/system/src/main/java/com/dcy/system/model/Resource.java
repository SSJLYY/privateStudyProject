package com.dcy.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dcy.common.model.TreeModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 资源表
 * </p>
 *
 * @author dcy
 * @since 2020-08-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_resource")
public class Resource implements TreeModel<Resource>, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 父级id
     */
    private String parentId;

    /**
     * 父级ids
     */
    private String parentIds;

    /**
     * 标题（目录名称、菜单名称、按钮名称）
     */
    private String title;

    /**
     * 类型（1、目录；2、菜单；3、按钮）
     *
     * @see com.dcy.system.enums.ResourcesTypeEnum
     */
    private String type;

    /**
     * 权限标识（菜单和按钮）
     */
    private String permission;

    /**
     * 后端url路径地址（菜单和按钮）
     */
    private String resPath;

    /**
     * 请求方式（GET或者POST等等）
     */
    private String httpMethod;

    /**
     * 路由地址（目录和菜单）
     */
    private String routePath;

    /**
     * 菜单组件名称
     */
    private String componentName;

    /**
     * 菜单组件地址
     */
    private String componentPath;

    /**
     * 状态（0、正常；1、禁用）
     *
     * @see com.dcy.system.enums.ResourceStatusEnum
     */
    private String resStatus;

    /**
     * 排序
     */
    private BigDecimal resSort;

    /**
     * 外链菜单（1：是；2：否）
     *
     * @see com.dcy.system.enums.ResourceMenuExtFlagEnum
     */
    private String menuExtFlag;

    /**
     * 菜单缓存（1：是；2：否）
     *
     * @see com.dcy.system.enums.ResourceMenuCacheFlagEnum
     */
    private String menuCacheFlag;

    /**
     * 菜单和目录可见（1：是；2：否）
     *
     * @see com.dcy.system.enums.ResourceMenuHiddenFlagEnum
     */
    private String menuHiddenFlag;

    /**
     * 菜单图标
     */
    private String menuIcon;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 子级数据
     */
    @TableField(exist = false)
    private List<Resource> children;


    public static final String ID = "id";

    public static final String PARENT_ID = "parent_id";

    public static final String PARENT_IDS = "parent_ids";

    public static final String TITLE = "title";

    public static final String TYPE = "type";

    public static final String PERMISSION = "permission";

    public static final String RES_PATH = "res_path";

    public static final String HTTP_METHOD = "http_method";

    public static final String ROUTE_PATH = "route_path";

    public static final String COMPONENT_NAME = "component_name";

    public static final String COMPONENT_PATH = "component_path";

    public static final String RES_STATUS = "res_status";

    public static final String RES_SORT = "res_sort";

    public static final String MENU_EXT_FLAG = "menu_ext_flag";

    public static final String MENU_CACHE_FLAG = "menu_cache_flag";

    public static final String MENU_HIDDEN_FLAG = "menu_hidden_flag";

    public static final String MENU_ICON = "menu_icon";

    public static final String CREATE_DATE = "create_date";
}

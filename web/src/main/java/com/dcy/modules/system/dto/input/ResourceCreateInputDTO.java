package com.dcy.modules.system.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/10/23 15:01
 */
@Getter
@Setter
@ApiModel(value = "ResourcesCreateInputDTO", description = "创建资源使用")
public class ResourceCreateInputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "父级id")
    private String parentId;

    @ApiModelProperty(value = "父级ids")
    private String parentIds;

    @ApiModelProperty(value = "标题（目录名称、菜单名称、按钮名称）")
    private String title;

    @ApiModelProperty(value = "类型（1、目录；2、菜单；3、按钮）")
    private String type;

    @ApiModelProperty(value = "权限标识（菜单和按钮）")
    private String permission;

    @ApiModelProperty(value = "后端url路径地址（菜单和按钮）")
    private String resPath;

    @ApiModelProperty(value = "请求方式（GET或者POST等等）")
    private String httpMethod;

    @ApiModelProperty(value = "路由地址（目录和菜单）")
    private String routePath;

    @ApiModelProperty(value = "菜单组件名称")
    private String componentName;

    @ApiModelProperty(value = "菜单组件地址")
    private String componentPath;

    @ApiModelProperty(value = "状态（0、正常；1、禁用）")
    private String resStatus;

    @ApiModelProperty(value = "排序")
    private BigDecimal resSort;

    @ApiModelProperty(value = "外链菜单（1：是；2：否）")
    private String menuExtFlag;

    @ApiModelProperty(value = "菜单缓存（1：是；2：否）")
    private String menuCacheFlag;

    @ApiModelProperty(value = "菜单和目录可见（1：是；2：否）")
    private String menuHiddenFlag;

    @ApiModelProperty(value = "菜单图标")
    private String menuIcon;
}

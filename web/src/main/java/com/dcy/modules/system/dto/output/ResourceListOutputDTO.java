package com.dcy.modules.system.dto.output;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

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
 * @since 2021-03-19
 */
@Getter
@Setter
@ApiModel(value = "ResourcesListOutputDTO对象", description = "资源表格")
public class ResourceListOutputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

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

    @ApiModelProperty(value = "路由地址（目录和菜单）", notes = "相当于path")
    private String routePath;

    @ApiModelProperty(value = "菜单组件名称", notes = "相当于name")
    private String componentName;

    @ApiModelProperty(value = "菜单组件地址", notes = "相当于component")
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

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "子级数据")
    @JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue)
    private List<ResourceListOutputDTO> children;
}

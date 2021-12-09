package com.dcy.modules.system.dto.output;

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
@ApiModel(value = "RouterListOutputDTO", description = "路由地址")
public class RouterListOutputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜单组件名称", notes = "相当于name")
    private String name;

    @ApiModelProperty(value = "路由地址（目录和菜单）", notes = "相当于path")
    private String path;

    @ApiModelProperty(value = "菜单组件地址", notes = "相当于component")
    private String component;

    @ApiModelProperty(value = "是否只显示一个")
    private Boolean alwaysShow;

    @ApiModelProperty(value = "菜单和目录可见（1：是；2：否）")
    private Boolean hidden;

    @ApiModelProperty(value = "重定向")
    private String redirect;

    @ApiModelProperty(value = "元信息")
    private MetaOutputDTO meta;

    @ApiModelProperty(value = "子级数据")
    private List<RouterListOutputDTO> children;

    @Getter
    @Setter
    public static class MetaOutputDTO {

        @ApiModelProperty(value = "标题")
        private String title;

        @ApiModelProperty(value = "图标")
        private String icon;

        @ApiModelProperty(value = "缓存")
        private Boolean noCache;
    }
}



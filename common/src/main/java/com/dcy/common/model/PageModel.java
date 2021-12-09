package com.dcy.common.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author：dcy
 * @Description: 分页类
 * @Date: 2020/11/26 8:33
 */
@Getter
@Setter
public class PageModel {

    @ApiModelProperty(value = "当前页面", notes = "默认1", example = "1")
    private long current = 1;

    @ApiModelProperty(value = "每页显示条数", notes = "默认30", example = "30")
    private long size = 30;

    @ApiModelProperty(value = "排序字段", notes = "对于model字段")
    private String sort;

    @ApiModelProperty(value = "排序类型", notes = "asc 或者 desc", allowableValues = "asc,desc")
    private String order;
}

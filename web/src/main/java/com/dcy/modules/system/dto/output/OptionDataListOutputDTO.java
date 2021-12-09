package com.dcy.modules.system.dto.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 流程节点表
 * </p>
 *
 * @author dcy
 * @since 2021-06-08
 */
@Getter
@Setter
@ApiModel(value = "OptionDataListOutputDTO", description = "审批节点数据列表")
public class OptionDataListOutputDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    public OptionDataListOutputDTO() {
    }

    public OptionDataListOutputDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }
}

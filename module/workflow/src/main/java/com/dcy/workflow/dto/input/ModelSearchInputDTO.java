package com.dcy.workflow.dto.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/6/7 13:08
 */
@Getter
@Setter
@ApiModel(value = "ModelSearchInputDTO", description = "模型查询表单")
public class ModelSearchInputDTO {

    @ApiModelProperty("类别")
    private String category;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("key")
    private String key;

}

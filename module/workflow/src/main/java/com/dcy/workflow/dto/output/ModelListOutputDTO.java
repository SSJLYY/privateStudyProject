package com.dcy.workflow.dto.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.flowable.engine.repository.Model;

import java.util.Date;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/6/7 13:03
 */
@Getter
@Setter
@ApiModel(value = "ModelListOutputDTO")
public class ModelListOutputDTO {

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "业务key")
    private String key;

    @ApiModelProperty(value = "类别")
    private String category;

    @ApiModelProperty(value = "版本")
    private Integer version;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "最后修改时间")
    private Date lastUpdateTime;

    public ModelListOutputDTO() {

    }

    public ModelListOutputDTO(Model model) {
        this.id = model.getId();
        this.name = model.getName();
        this.key = model.getKey();
        this.category = model.getCategory();
        this.createTime = model.getCreateTime();
        this.lastUpdateTime = model.getLastUpdateTime();
        this.version = model.getVersion();
    }

}

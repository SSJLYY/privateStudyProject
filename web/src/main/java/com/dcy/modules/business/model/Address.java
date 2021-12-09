package com.dcy.modules.business.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/9/9 13:17
 */
@Getter
@Setter
@ToString
public class Address {

    @NotBlank(message = "Address地址不能为空")
    private String name;
}

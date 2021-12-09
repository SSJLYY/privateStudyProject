package com.dcy.modules.business.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/9/9 13:17
 */
@Getter
@Setter
@ToString
public class UserVal {

    @NotBlank(message = "用户名不能为空")
    private String username;
    private String password;

    /**
     * 嵌套对象校验
     */
    @Valid
    private Address address;

    /**
     * 嵌套对象校验
     */
    @Valid
    @NotEmpty(message = "地址长度不能为空")
    private List<Address> addressList;
}

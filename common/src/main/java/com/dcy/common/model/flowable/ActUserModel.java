package com.dcy.common.model.flowable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/6/11 9:09
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ActUserModel {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickName;

}

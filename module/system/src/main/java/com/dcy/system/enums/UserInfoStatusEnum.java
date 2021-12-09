package com.dcy.system.enums;

import java.util.stream.Stream;

/**
 * @Author：dcy
 * @Description: 用户状态枚举类
 * @Date: 2021/8/24 11:08
 */
public enum UserInfoStatusEnum {

    //用户 帐号状态（0、正常；1、禁用）
    NORMAL("0", "正常"),
    DISABLE("1", "禁用"),
    ;

    public final String code;
    public final String name;

    UserInfoStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code取枚举对象
     *
     * @param code
     * @return
     */
    public static UserInfoStatusEnum getByCode(String code) {
        return Stream.of(UserInfoStatusEnum.values())
                .filter(userInfoStatusEnum -> userInfoStatusEnum.code.equals(code))
                .findAny()
                .orElse(null);
    }

}

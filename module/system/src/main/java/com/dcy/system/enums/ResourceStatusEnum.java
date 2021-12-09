package com.dcy.system.enums;

import java.util.stream.Stream;

/**
 * @Author：dcy
 * @Description: 资源状态枚举类
 * @Date: 2021/8/24 11:04
 */
public enum ResourceStatusEnum {

    //资源 状态（0、正常；1、禁用）
    NORMAL("0", "正常"),
    DISABLE("1", "禁用"),
    ;

    public final String code;
    public final String name;

    ResourceStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code取枚举对象
     *
     * @param code
     * @return
     */
    public static ResourceStatusEnum getByCode(String code) {
        return Stream.of(ResourceStatusEnum.values())
                .filter(resourcesStatusEnum -> resourcesStatusEnum.code.equals(code))
                .findAny()
                .orElse(null);
    }

}

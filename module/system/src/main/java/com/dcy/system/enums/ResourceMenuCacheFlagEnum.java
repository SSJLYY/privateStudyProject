package com.dcy.system.enums;

import java.util.stream.Stream;

/**
 * @Author：dcy
 * @Description: 资源状态枚举类
 * @Date: 2021/8/24 11:04
 */
public enum ResourceMenuCacheFlagEnum {

    //资源 菜单缓存（1：是；2：否）
    YES("1", "是"),
    NO("2", "否"),
    ;

    public final String code;
    public final String name;

    ResourceMenuCacheFlagEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code取枚举对象
     *
     * @param code
     * @return
     */
    public static ResourceMenuCacheFlagEnum getByCode(String code) {
        return Stream.of(ResourceMenuCacheFlagEnum.values())
                .filter(resourceMenuExtFlagEnum -> resourceMenuExtFlagEnum.code.equals(code))
                .findAny()
                .orElse(null);
    }

}

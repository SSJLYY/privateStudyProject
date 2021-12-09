package com.dcy.system.enums;

import java.util.stream.Stream;

/**
 * @Author：dcy
 * @Description: 资源类型枚举类
 * @Date: 2021/8/24 11:04
 */
public enum ResourcesTypeEnum {

    //资源 类型（1、目录；2、菜单；3、按钮）
    CATALOGUE("1", "目录"),
    MENU("2", "菜单"),
    BUTTON("3", "按钮"),
    ;

    public final String code;
    public final String name;

    ResourcesTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code取枚举对象
     *
     * @param code
     * @return
     */
    public static ResourcesTypeEnum getByCode(String code) {
        return Stream.of(ResourcesTypeEnum.values())
                .filter(resourcesTypeEnum -> resourcesTypeEnum.code.equals(code))
                .findAny()
                .orElse(null);
    }
}

package com.dcy.system.enums;

import java.util.stream.Stream;

/**
 * @Author：dcy
 * @Description: 字典数据状态枚举类
 * @Date: 2021/8/24 11:10
 */
public enum DictDataStatusEnum {

    //字典数据 状态（0正常 1停用）
    NORMAL("0", "正常"),
    DEACTIVATE("1", "停用"),
    ;

    public final String code;
    public final String name;

    DictDataStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code取枚举对象
     *
     * @param code
     * @return
     */
    public static DictDataStatusEnum getByCode(String code) {
        return Stream.of(DictDataStatusEnum.values())
                .filter(dictDataStatusEnum -> dictDataStatusEnum.code.equals(code))
                .findAny()
                .orElse(null);
    }
}

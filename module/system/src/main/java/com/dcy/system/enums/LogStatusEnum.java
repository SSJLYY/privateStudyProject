package com.dcy.system.enums;

import java.util.stream.Stream;

/**
 * @Author：dcy
 * @Description: 日志状态枚举类
 * @Date: 2021/8/24 11:06
 */
public enum LogStatusEnum {

    //日志 操作状态（0正常 1异常）
    NORMAL("0", "正常"),
    ABNORMAL("1", "异常"),
    ;

    public final String code;
    public final String name;

    LogStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code取枚举对象
     *
     * @param code
     * @return
     */
    public static LogStatusEnum getByCode(String code) {
        return Stream.of(LogStatusEnum.values())
                .filter(logStatusEnum -> logStatusEnum.code.equals(code))
                .findAny()
                .orElse(null);
    }
}

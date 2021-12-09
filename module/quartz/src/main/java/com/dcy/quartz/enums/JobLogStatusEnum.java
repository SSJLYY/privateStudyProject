package com.dcy.quartz.enums;

import cn.hutool.core.util.StrUtil;

/**
 * @Author：dcy
 * @Description: 定时任务调度日志状态枚举类
 * @Date: 2021/8/24 11:01
 */
public enum JobLogStatusEnum {

    //执行状态（0正常 1失败）
    NORMAL("0", "正常"),
    FAIL("1", "失败"),
    ;

    public final String code;
    public final String name;

    JobLogStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code取枚举对象
     *
     * @param code
     * @return
     */
    public static JobLogStatusEnum getByCode(String code) {
        if (StrUtil.isBlank(code)) {
            return null;
        }
        for (JobLogStatusEnum enums : JobLogStatusEnum.values()) {
            if (enums.code.equals(code)) {
                return enums;
            }
        }
        return null;
    }

}

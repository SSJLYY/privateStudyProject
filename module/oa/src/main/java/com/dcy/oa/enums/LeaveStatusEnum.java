package com.dcy.oa.enums;

import cn.hutool.core.util.StrUtil;

/**
 * @Author：dcy
 * @Description: 请假状态枚举类
 * @Date: 2021/8/24 10:53
 */
public enum LeaveStatusEnum {

    //状态（1：未开始；2：进行中；3：已完成；4：已驳回）
    NOT_STARTED("1", "未开始"),
    HAVE_IN_HAND("2", "进行中"),
    COMPLETED("3", "已完成"),
    REJECT("4", "已驳回"),
    ;

    public final String code;
    public final String name;

    LeaveStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code取枚举对象
     *
     * @param code
     * @return
     */
    public static LeaveStatusEnum getByCode(String code) {
        if (StrUtil.isBlank(code)) {
            return null;
        }
        for (LeaveStatusEnum enums : LeaveStatusEnum.values()) {
            if (enums.code.equals(code)) {
                return enums;
            }
        }
        return null;
    }

}

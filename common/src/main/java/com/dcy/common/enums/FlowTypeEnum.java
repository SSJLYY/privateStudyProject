package com.dcy.common.enums;

import cn.hutool.core.util.StrUtil;

public enum FlowTypeEnum {

    // 流程审批类型（1：用户；2：角色；3：部门）
    USER("1", "用户审批"),
    ROLE("2", "角色审批"),
    DEPT("3", "部门审批"),
    ;

    public final String code;
    public final String name;

    FlowTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code取枚举对象
     *
     * @param code
     * @return
     */
    public static FlowTypeEnum getByCode(String code) {
        if (StrUtil.isBlank(code)) {
            return null;
        }
        for (FlowTypeEnum enums : FlowTypeEnum.values()) {
            if (enums.code.equals(code)) {
                return enums;
            }
        }
        return null;
    }
}

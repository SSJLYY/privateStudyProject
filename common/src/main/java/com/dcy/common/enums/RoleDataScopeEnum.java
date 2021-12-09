package com.dcy.common.enums;

import cn.hutool.core.util.StrUtil;

public enum RoleDataScopeEnum {

    // 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
    ALL("1", "全部数据权限"),
    CUSTOM("2", "自定数据权限"),
    ME_DEPT("3", "本部门数据权限"),
    ME_DEPT_CHILD("4", "本部门及以下数据权限");

    public final String code;
    public final String name;

    RoleDataScopeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code取枚举对象
     *
     * @param code
     * @return
     */
    public static RoleDataScopeEnum getByCode(String code) {
        if (StrUtil.isBlank(code)) {
            return null;
        }
        for (RoleDataScopeEnum enums : RoleDataScopeEnum.values()) {
            if (enums.code.equals(code)) {
                return enums;
            }
        }
        return null;
    }
}

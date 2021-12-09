package com.dcy.db.base.model;

/**
 * @Author：dcy
 * @Description: 角色权限model
 * @Date: 2021/9/3 14:29
 */
public interface DataScopeRoleModel {

    /**
     * 角色id
     *
     * @return 角色id
     */
    String getId();

    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
     *
     * @return 数据范围
     * @see com.dcy.common.enums.RoleDataScopeEnum
     */
    String getDataScope();

}

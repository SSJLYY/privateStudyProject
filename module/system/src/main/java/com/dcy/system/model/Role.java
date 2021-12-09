package com.dcy.system.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dcy.db.base.model.DataScopeRoleModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author dcy
 * @since 2020-08-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role")
public class Role implements DataScopeRoleModel, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    private String id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限字符串
     */
    private String roleKey;

    /**
     * 角色状态（0、正常；1、禁用）
     */
    private String roleStatus;

    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
     *
     * @see com.dcy.common.enums.RoleDataScopeEnum
     */
    private String dataScope;

    public static final String ID = "id";

    public static final String ROLE_NAME = "role_name";

    public static final String ROLE_KEY = "role_key";

    public static final String ROLE_STATUS = "role_status";

    public static final String DATA_SCOPE = "data_scope";

}

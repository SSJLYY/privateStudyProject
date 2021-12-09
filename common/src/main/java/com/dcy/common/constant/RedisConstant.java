package com.dcy.common.constant;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/11/19 9:35
 */
public interface RedisConstant {

    /**
     * 角色数据权限
     */
    String REDIS_ROLE_DATA_SCOPE = "role:data-scope";
    /**
     * 角色数据权限标识
     */
    String REDIS_ROLE_DATA_SCOPE_FLAG = "role:data-scope-flag";
    /**
     * 用户角色
     */
    String REDIS_USER_ROLE = "user:role";
    /**
     * 用户资源
     */
    String REDIS_USER_RESOURCE = "user:resource";
    /**
     * 资源路由
     */
    String REDIS_RESOURCE_ROUTER = "resource:router";

}

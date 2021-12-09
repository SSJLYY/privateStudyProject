package com.dcy.common.constant;

/**
 * @Author：dcy
 * @Description: 全局公共常量
 * @Date: 2019/9/6 13:36
 */
public interface Constant {

    String SESSION_USER_KEY = "UserInfo";
    /**
     * 存储用户权限
     */
    String REDIS_CAPTCHA_KEY = "user:captcha:";

    String COMMON_PARENT_ID = "0";
    String ROUTER_LAYOUT = "Layout";
    String ROUTER_PARENT_VIEW = "ParentView";
    String ROUTER_NO_REDIRECT = "noRedirect";
}

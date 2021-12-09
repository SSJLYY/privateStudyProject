package com.dcy.enums;

import com.dcy.common.api.IErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 接口返回枚举
 *
 * @author dcy
 */
@AllArgsConstructor
@Getter
public enum AdminApiErrorCode implements IErrorCode {
    // token错误
    TOKEN_ERROR(2000, "token错误"),
    NO_TOKEN_ERROR(2001, "没有token，请重新登录"),
    USER_PASSWORD_ERROR(2002, "用户名和密码错误，请重新输入"),
    USER_UPDATE_PASS_ERROR(2005, "当前密码输入错误，请重新输入"),
    USER_UPDATE_PASS2_ERROR(2006, "两次密码输入不一致，请重新输入"),
    USER_LOCKED_ERROR(2003, "用户已锁定"),
    USER_NOROLE_LOCKED_ERROR(2004, "没有角色"),
    USER_NOPERMISSION_LOCKED_ERROR(2005, "没有权限"),
    ;

    private final Integer code;
    private final String msg;

}

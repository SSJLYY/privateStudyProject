package com.dcy.handler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.dcy.common.model.R;
import com.dcy.enums.AdminApiErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author：dcy
 * @Description: 用户错误相关错误处理
 * @Date: 2021/6/2 8:39
 */
@RestControllerAdvice
@Order(-5)
@Slf4j
public class TokenExceptionHandler {

    /**
     * token相关异常
     *
     * @param notLoginException
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NotLoginException.class)
    public R<String> handlerNotLoginException(NotLoginException notLoginException) {
        log.warn("handlerNotLoginException [{}]", notLoginException.toString());
        // 打印堆栈，以供调试
        notLoginException.printStackTrace();
        // 判断场景值，定制化异常信息
        String message = "";
        switch (notLoginException.getType()) {
            case NotLoginException.NOT_TOKEN:
                message = NotLoginException.NOT_TOKEN_MESSAGE;
                break;
            case NotLoginException.INVALID_TOKEN:
                message = NotLoginException.INVALID_TOKEN_MESSAGE;
                break;
            case NotLoginException.TOKEN_TIMEOUT:
                message = NotLoginException.TOKEN_TIMEOUT_MESSAGE;
                break;
            case NotLoginException.BE_REPLACED:
                message = NotLoginException.BE_REPLACED_MESSAGE;
                break;
            case NotLoginException.KICK_OUT:
                message = NotLoginException.KICK_OUT_MESSAGE;
                break;
            default:
                message = NotLoginException.DEFAULT_MESSAGE;
                break;
        }
        return R.restResult(AdminApiErrorCode.TOKEN_ERROR, message);
    }

    /**
     * 没有角色
     *
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NotRoleException.class)
    public R<String> handlerNotRoleException(NotRoleException exception) {
        log.warn("handlerNotRoleException [{}]", exception.toString());
        return R.restResult(AdminApiErrorCode.USER_NOROLE_LOCKED_ERROR, exception.getMessage());
    }

    /**
     * 没有权限
     *
     * @param exception
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NotPermissionException.class)
    public R<String> handlerNotPermissionException(NotPermissionException exception) {
        log.warn("handlerNotPermissionException [{}]", exception.toString());
        return R.restResult(AdminApiErrorCode.USER_NOPERMISSION_LOCKED_ERROR, exception.getMessage());
    }


}

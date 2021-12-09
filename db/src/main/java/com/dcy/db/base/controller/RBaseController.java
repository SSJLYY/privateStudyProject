package com.dcy.db.base.controller;

import com.dcy.common.api.IErrorCode;
import com.dcy.common.context.BaseContextHandler;
import com.dcy.common.enums.ApiErrorCode;
import com.dcy.common.model.R;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/4/20 16:30
 */
public class RBaseController {

    /**
     * 获取用户id
     */
    protected String getUserId() {
        return BaseContextHandler.getUserID();
    }

    /**
     * 获取用户名称
     */
    protected String getUsername() {
        return BaseContextHandler.getUsername();
    }

    protected <T> R<T> success() {
        return R.restResult(ApiErrorCode.SUCCESS, null);
    }

    protected <T> R<T> success(T obj) {
        return R.success(obj);
    }

    protected <T> R<T> error(String msg) {
        return R.error(msg);
    }

    protected <T> R<T> error(IErrorCode errorCode, T obj) {
        return R.error(errorCode);
    }

    protected <T> R<T> restResult(IErrorCode errorCode, T data) {
        return R.restResult(errorCode, data);
    }

}

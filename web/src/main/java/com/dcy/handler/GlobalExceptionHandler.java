package com.dcy.handler;

import com.dcy.common.enums.ApiErrorCode;
import com.dcy.common.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author：dcy
 * @Description: 全局的的异常拦截器（拦截所有的控制器）
 * @Date: 2019/9/6 13:25
 */
@RestControllerAdvice
@Order(-1)
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 所有异常信息
     *
     * @param exception
     * @return
     * @throws Exception
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public R<String> exceptionHandler(Exception exception) {
        log.error("exceptionHandle [{}]", exception.toString());
        exception.printStackTrace();
        return R.restResult(ApiErrorCode.OTHER_ERROR, exception.getMessage());
    }


}

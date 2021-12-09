package com.dcy.aspect;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.dcy.common.constant.Constant;
import com.dcy.common.context.BaseContextHandler;
import com.dcy.modules.system.dto.output.LoginOutputDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Author：dcy
 * @Description: 获取session中的用户信息
 * @Date: 2021/5/8 14:21
 */
@Aspect
@Slf4j
@Component
@Order(-200)
public class BaseContextAspect {

    /**
     * 切入点
     */
    @Pointcut("execution(public * com.dcy.modules.*.controller..*(..)) " +
            " || execution(public * com.dcy.*.controller..*(..))" +
            " || execution(public * com.dcy.db.base.controller..*(..))")
    public void controllerMethod() {
    }

    /**
     * 前置通知
     *
     * @param joinPoint
     * @throws Exception
     * @since set手机端相关信息
     */
    @Before("controllerMethod()")
    public void logRequestInfo(JoinPoint joinPoint) throws Exception {
        // 设置用户信息
        if (StpUtil.isLogin()){
            SaSession session = StpUtil.getSession(false);
            LoginOutputDTO loginOutputDTO = session.getModel(Constant.SESSION_USER_KEY, LoginOutputDTO.class);
            BaseContextHandler.setUserID(loginOutputDTO.getId());
            BaseContextHandler.setUsername(loginOutputDTO.getUsername());
        }
    }

    /**
     * 后置通知
     */
    @AfterReturning(returning = "rvt", pointcut = "controllerMethod()")
    public void logResultVoInfo(Object rvt) throws Exception {
        BaseContextHandler.remove();
    }

    /**
     * 异常通知
     *
     * @param joinPoint
     * @param exception
     */
    @AfterThrowing(value = "controllerMethod()", throwing = "exception")
    public void doAfterThrowingAdvice(JoinPoint joinPoint, Throwable exception) {
        BaseContextHandler.remove();
    }

}

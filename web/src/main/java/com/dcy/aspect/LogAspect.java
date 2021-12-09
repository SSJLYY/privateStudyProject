package com.dcy.aspect;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.dcy.common.context.BaseContextHandler;
import com.dcy.system.enums.LogStatusEnum;
import com.dcy.system.model.Log;
import com.dcy.system.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author：dcy
 * @Description: 日志aop
 * @Date: 2019/10/15 9:05
 */
@Aspect
@Slf4j(topic = "request-log")
@Component
@Order(1)
public class LogAspect {

    @Autowired
    private LogService logService;

    /**
     * ..表示包及子包 该方法代表controller层的所有方法
     * Pointcut定义时，还可以使用&&、||、! 这三个运算
     */
    @Pointcut("@annotation(com.dcy.common.annotation.Log)")
    public void controllerMethod() {
    }

    /**
     * 后置通知
     */
    @AfterReturning(pointcut = "controllerMethod()", returning = "result")
    public void logResultVoInfo(JoinPoint joinPoint, Object result) {
        handleLog(joinPoint, null, result);
    }

    /**
     * 异常通知
     *
     * @param joinPoint
     * @param exception
     */
    @AfterThrowing(value = "controllerMethod()", throwing = "exception")
    public void doAfterThrowingAdvice(JoinPoint joinPoint, Exception exception) {
        handleLog(joinPoint, exception, null);
    }

    /**
     * 处理log
     *
     * @param joinPoint
     * @param exception
     * @param result
     */
    protected void handleLog(final JoinPoint joinPoint, final Exception exception, Object result) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        Log operationalLog = new Log();
        operationalLog.setIp(ServletUtil.getClientIP(request));
        operationalLog.setUrl(request.getRequestURI());
        operationalLog.setMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        operationalLog.setOperName(BaseContextHandler.getUsername());
        setBusinessName(joinPoint, targetMethod, operationalLog);
        if (joinPoint.getArgs().length == 0) {
            operationalLog.setOperParam("{}");
        } else if (joinPoint.getArgs().length == 1) {
            Object arg = joinPoint.getArgs()[0];
            // 是否实现校验父接口
            if (!(arg instanceof ServletRequest || arg instanceof ServletResponse || arg instanceof MultipartFile)) {
                operationalLog.setOperParam(JSON.toJSONString(arg));
            }
        } else {
            List<Object> list = new ArrayList<>();
            for (Object arg : joinPoint.getArgs()) {
                // 是否实现校验父接口
                if (arg instanceof ServletRequest || arg instanceof ServletResponse || arg instanceof MultipartFile) {
                    continue;
                }
                list.add(arg);
            }
            operationalLog.setOperParam(JSON.toJSONString(list));
        }
        if (result != null) {
            operationalLog.setResult(JSON.toJSONString(result));
        }
        if (exception != null) {
            operationalLog.setLogStatus(LogStatusEnum.ABNORMAL.code);
            operationalLog.setError(exception.getMessage());
        }
        log.info(JSON.toJSONString(operationalLog));
        logService.saveLog(operationalLog);
    }


    /**
     * 设置业务模块
     *
     * @param joinPoint
     * @param targetMethod
     * @param operationalLog
     */
    private void setBusinessName(JoinPoint joinPoint, Method targetMethod, Log operationalLog) {
        // 类注解
        Api api = AnnotationUtil.getAnnotation(joinPoint.getSignature().getDeclaringType(), Api.class);
        // 方法注解
        ApiOperation annotation = AnnotationUtil.getAnnotation(targetMethod, ApiOperation.class);
        if (api.tags().length > 0) {
            operationalLog.setBusinessName(CollUtil.join(Arrays.asList(api.tags()), ",") + ":" + annotation.value());
        } else {
            operationalLog.setBusinessName(annotation.value());
        }
    }
}

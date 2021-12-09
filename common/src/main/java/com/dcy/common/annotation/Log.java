package com.dcy.common.annotation;

import java.lang.annotation.*;

/**
 * @author dcy
 * @Date: 2021/3/19 10:17
 * @Description: 打印日志
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
}

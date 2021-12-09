package com.dcy.db.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author：dcy
 * @Description: 数据权限过滤注解
 * @Date: 2021/9/8 15:18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD
})
public @interface DataScope {

    /**
     * 区分类型 用于不同类型过滤字段不同
     *
     * @return
     */
    String type() default "dev";

    /**
     * 过滤字段
     *
     * @return
     */
    DataColumn[] value();
}

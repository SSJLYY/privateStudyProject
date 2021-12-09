package com.dcy.db.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author：dcy
 * @Description: 数据权限过滤字段
 * @Date: 2021/9/8 15:18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD
})
public @interface DataColumn {

    /**
     * 别名
     *
     * @return
     */
    String alias();

    /**
     * 字段名
     *
     * @return
     */
    String name();
}

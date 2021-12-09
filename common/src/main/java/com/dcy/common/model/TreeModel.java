package com.dcy.common.model;

import java.util.List;

/**
 * 树形
 *
 * @author Tango
 * @since 2020/11/11
 */
public interface TreeModel<T> {

    /**
     * 主键id
     * @return
     */
    String getId();

    /**
     * 父id
     * @return
     */
    String getParentId();

    /**
     * 设置子数据集
     * @param children
     * @return
     */
    T setChildren(List<T> children);
}

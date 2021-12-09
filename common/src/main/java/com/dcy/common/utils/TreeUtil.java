package com.dcy.common.utils;


import com.dcy.common.model.TreeModel;

import java.util.*;
import java.util.function.ToIntFunction;

/**
 * Model工具
 *
 * @author Tango
 * @since 2020/11/11
 */
public final class TreeUtil {
    private TreeUtil() {
    }

    /**
     * 列表转树形
     *
     * @param <T> {@link TreeModel}
     * @return 树
     */
    public static <T extends TreeModel<T>> List<T> listToTree(List<T> list) {
        return listToTree(list, null);
    }

    /**
     * 列表转树形 带排序
     *
     * @param list    所有list
     * @param compare {@link Comparator#comparingInt(ToIntFunction)}
     * @param <T>     {@link TreeModel}
     * @return 树
     */
    public static <T extends TreeModel<T>> List<T> listToTree(List<T> list, Comparator<T> compare) {
        //all groupingBy methods end up using the same map.merge() that rejects null keys
        //        Stream<T> stream = list.stream();
        Map<String, List<T>> groupMap = new HashMap<>();
        Map<String, T> idMap = new HashMap<>();
        List<T> result = new ArrayList<>();

        //按parent分组
        for (T t : list) {
            idMap.put(t.getId(), t);
            List<T> children = groupMap.computeIfAbsent(t.getParentId(), k -> new ArrayList<>());
            children.add(t);
        }
        //排序
        if (compare != null) {
            for (HashMap.Entry<String, List<T>> entry : groupMap.entrySet()) {
                String key = entry.getKey();
                List<T> value = entry.getValue();
                value.sort(compare);
                matchChildren(key, value, idMap, result);
            }
            result.sort(compare);
        }
        //不排序
        else {
            for (HashMap.Entry<String, List<T>> entry : groupMap.entrySet()) {
                String key = entry.getKey();
                List<T> value = entry.getValue();
                matchChildren(key, value, idMap, result);
            }
        }
        return result;
    }

    //给children

    /**
     * 给children
     * 2020/11/16 null key 无父级按根级处理
     */
    private static <T extends TreeModel<T>> void matchChildren(String key, List<T> value, Map<String, T> idMap, List<T> result) {
        T t = idMap.get(key);
        if (t != null) {
            t.setChildren(value);
        } else {
            result.addAll(value);
        }
    }
}

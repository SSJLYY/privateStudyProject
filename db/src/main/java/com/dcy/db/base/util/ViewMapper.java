package com.dcy.db.base.util;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @Author：dcy
 * @Description: lamda方式关联表
 * @Date: 2021/10/23 7:53
 */
public class ViewMapper<DTO, K, V> {

    private Function<Collection<K>, Map<K, V>> viewReference;
    private Function<DTO, K> getKey;
    private BiFunction<DTO, V, DTO> mapping;

    /**
     * 存储关联key
     */
    private Set<K> keySet = new HashSet<>();
    /**
     * 存储key -> value
     */
    private Map<K, V> valueMap = new HashMap<>();

    /**
     * @param viewReference 根据关联表主键id查询，返回Map对象
     * @param getKey        根据dto 把关联表的key拿出来
     * @param mapping       根据dto 和 value 映射赋值
     */
    public ViewMapper(Function<Collection<K>, Map<K, V>> viewReference, Function<DTO, K> getKey, BiFunction<DTO, V, DTO> mapping) {
        this.viewReference = viewReference;
        this.getKey = getKey;
        this.mapping = mapping;
    }

    /**
     * 根据key 获取value
     */
    void storeValue() {
        if (!keySet.isEmpty()) {
            valueMap = viewReference.apply(keySet);
        }
    }

    /**
     * 根据dto 转换key
     *
     * @param dto
     */
    void storeKeys(DTO dto) {
        final K key = getKey.apply(dto);
        if (key != null) {
            keySet.add(key);
        }
    }

    /**
     * 执行方法
     *
     * @param dto
     */
    void apply(DTO dto) {
        // getKey.apply(dto) 根据dto 把key找出来
        // valueMap.get() 根据这个key 把map找出来
        // mapping.apply() 映射
        this.mapping.apply(dto, valueMap.get(getKey.apply(dto)));
    }


    public static <DTO, K, V> ViewMapper<DTO, K, V> setView(Function<Collection<K>, Map<K, V>> viewReference, Function<DTO, K> getKey, BiFunction<DTO, V, DTO> mapping) {
        return new ViewMapper<>(viewReference, getKey, mapping);
    }

    /**
     * 表关联
     * @param outputList
     * @param viewMapper
     * @param <DTO>
     * @param <K>
     * @param <V>
     * @return
     */
    public static <DTO, K, V> List<DTO> batchMapView(List<DTO> outputList, ViewMapper<DTO, K, V> viewMapper) {
        outputList.forEach(viewMapper::storeKeys);
        viewMapper.storeValue();
        outputList.forEach(viewMapper::apply);
        return outputList;
    }

    /**
     * 表关联
     * @param outputList
     * @param viewMapper
     * @param <DTO>
     * @param <K>
     * @param <V>
     * @return
     */
    @SafeVarargs
    public static <DTO, K, V> List<DTO> batchMapView(List<DTO> outputList, ViewMapper<DTO, K, V>... viewMapper) {
        final List<ViewMapper<DTO, K, V>> viewMappers = Arrays.asList(viewMapper);
        outputList.forEach(dto -> viewMappers.forEach(ViewMapper::storeValue));
        viewMappers.parallelStream().forEach(ViewMapper::storeValue);
        outputList.forEach(dto -> viewMappers.forEach(viewMapper1 -> viewMapper1.apply(dto)));
        return outputList;
    }

}

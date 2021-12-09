package com.dcy.generate.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/10/28 18:29
 */
@Data
@Accessors(chain = true)
public class PackageModel {

    /**
     * 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
     */
    private String parent = "com.dcy";
    /**
     * Entity包名
     */
    private String entity = "entity";
    /**
     * Service包名
     */
    private String service = "service";
    /**
     * Service Impl包名
     */
    private String serviceImpl = "service.impl";
    /**
     * Mapper包名
     */
    private String mapper = "mapper";
    /**
     * Mapper XML包名
     */
    private String xml = "mapper.xml";
    /**
     * Controller包名
     */
    private String controller = "controller";
    /**
     * input-dto包路径
     */
    private String inputDto;
    /**
     * out-dto包路径
     */
    private String outDto;
    /**
     * 转换类
     */
    private String dtoMapper;
}

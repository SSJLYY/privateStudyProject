package com.dcy.generate.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.setting.dialect.Props;
import com.dcy.generate.model.GenerModel;
import com.dcy.generate.model.PackageModel;

public class GenUtils {
    private static final Props DB_PROPS;
    private static final Props PACKAGE_PROPS;

    static {
        DB_PROPS = new Props("db.properties", CharsetUtil.UTF_8);
        PACKAGE_PROPS = new Props("package.properties", CharsetUtil.UTF_8);
    }

    public static GenerModel getGenerByProps() {
        GenerModel generModel = new GenerModel();
        generModel.setPack(DB_PROPS.getStr("db.pack"));
        generModel.setDbUrl(DB_PROPS.getStr("db.url"));
        generModel.setDriverName(DB_PROPS.getStr("db.driverName"));
        generModel.setUsername(DB_PROPS.getStr("db.username"));
        generModel.setPassword(DB_PROPS.getStr("db.password"));
        generModel.setTableName(DB_PROPS.getStr("table.tableName"));
        generModel.setPrefix(DB_PROPS.getStr("table.prefix"));
        return generModel;
    }

    public static PackageModel getPackageByProps() {
        PackageModel packageModel = new PackageModel();
        packageModel.setParent(PACKAGE_PROPS.getStr("package.parent"));
        packageModel.setEntity(PACKAGE_PROPS.getStr("package.entity"));
        packageModel.setService(PACKAGE_PROPS.getStr("package.service"));
        packageModel.setServiceImpl(PACKAGE_PROPS.getStr("package.serviceImpl"));
        packageModel.setMapper(PACKAGE_PROPS.getStr("package.mapper"));
        packageModel.setXml(PACKAGE_PROPS.getStr("package.xml"));
        packageModel.setController(PACKAGE_PROPS.getStr("package.controller"));
        packageModel.setInputDto(PACKAGE_PROPS.getStr("package.inputDto"));
        packageModel.setOutDto(PACKAGE_PROPS.getStr("package.outDto"));
        packageModel.setDtoMapper(PACKAGE_PROPS.getStr("package.dtoMapper"));
        return packageModel;
    }
}

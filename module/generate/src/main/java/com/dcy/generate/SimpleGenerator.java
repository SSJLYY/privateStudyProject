package com.dcy.generate;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.dcy.db.base.controller.BaseController;
import com.dcy.db.base.service.BaseService;
import com.dcy.generate.model.GenerModel;
import com.dcy.generate.model.PackageModel;
import com.dcy.generate.utils.GenUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author：dcy
 * @Description: 基础版代码生成器
 * @Date: 2020/7/22 10:25
 */
@Slf4j
public class SimpleGenerator {
    /**
     * RUN THIS
     */
    public static void main(String[] args) {
        final GenerModel generModel = GenUtils.getGenerByProps();
        final PackageModel packageModel = GenUtils.getPackageByProps();

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        //开发人员
        gc.setAuthor("dcy")
                // 是否覆盖已有文件
                .setFileOverride(true)
                //开启 BaseResultMap
                .setBaseResultMap(true)
                //开启 baseColumnList
                .setBaseColumnList(true)
                // 生成文件的输出目录
                .setOutputDir(generModel.getPack())
                // 是否打开输出目录
                .setOpen(false)
                // 时间类型对应策略
                .setDateType(DateType.ONLY_DATE)
                // 修改service名称
                .setServiceImplName("%sService");

        /*String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "\\web\\src\\main\\java");*/
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(generModel.getDbUrl())
                .setDriverName(generModel.getDriverName())
                .setUsername(generModel.getUsername())
                .setPassword(generModel.getPassword());
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(packageModel.getParent())
                .setController(packageModel.getController())
                .setEntity(packageModel.getEntity())
                .setService(packageModel.getService())
                .setServiceImpl(packageModel.getServiceImpl())
                .setMapper(packageModel.getMapper())
                .setXml(packageModel.getXml());
        mpg.setPackageInfo(pc);

        // 自定义生成文件
        setTemplateMapper(mpg, generModel, packageModel);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //下划线转驼峰命名
        strategy.setNaming(NamingStrategy.underline_to_camel)
                // 是否为lombok模型
                .setEntityLombokModel(true)
                // 自定义继承的Controller类全称
                .setSuperControllerClass(BaseController.class)
                // 自定义继承的Entity类全称
//                .setSuperEntityClass(PageModel.class)
                // 自定义基础的Entity类，公共字段
//                .setSuperEntityColumns("create_by","create_date","update_by","update_date","del_flag","remark")
                //自定义继承的ServiceImpl类全称
                .setSuperServiceImplClass(BaseService.class)
                // 需要包含的表名，允许正则表达式
                .setInclude(generModel.getTableName().split(","))
                //驼峰转连字符
                .setControllerMappingHyphenStyle(true)
                // 是否生成字段常量
                .setEntityColumnConstant(true)
                // 生成 <code>@RestController</code> 控制器
                .setRestControllerStyle(true)
                // 表前缀
                .setTablePrefix(generModel.getPrefix().split(","));
        mpg.setStrategy(strategy);


        // 模板引擎 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        // 模板路径配置
        TemplateConfig tc = new TemplateConfig();
        tc.setController("/simple/controller.java")
                .setService(null)
                .setServiceImpl("/simple/serviceImpl.java")
                .setEntity("/simple/entity.java")
                .setMapper("/simple/mapper.java")
                .setXml("/simple/mapper.xml");
        mpg.setTemplate(tc);

        // 生成代码
        mpg.execute();
    }

    private static void setTemplateMapper(AutoGenerator mpg, final GenerModel generModel, final PackageModel packageModel) {
        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】  ${cfg.abc}
        final String parent = mpg.getPackageInfo().getParent();
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("inputDtoPackage", parent + StringPool.DOT + packageModel.getInputDto());
                map.put("outputDtoPackage", parent + StringPool.DOT + packageModel.getOutDto());
                map.put("dtoMapperPackage", parent + StringPool.DOT + packageModel.getDtoMapper());
                this.setMap(map);
            }
        };
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        // DTO生成
        generDTO(focList, generModel, packageModel);
        // vue生成
        generVue(focList, generModel);
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
    }

    /**
     * 生成vue文件
     *
     * @param focList
     * @param generModel
     */
    private static void generVue(List<FileOutConfig> focList, final GenerModel generModel) {
        focList.add(new FileOutConfig("/vue/manage-element.vue.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return generModel.getPack() + "/vue/" + StrUtil.toSymbolCase(tableInfo.getEntityName(), '-') + "-manage.vue";
            }
        });
        focList.add(new FileOutConfig("/vue/vue.js.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return generModel.getPack() + "/vue/" + StrUtil.toSymbolCase(tableInfo.getEntityName(), '-') + ".js";
            }
        });
    }


    /**
     * 生成dto
     *
     * @param generModel
     * @param focList
     */
    private static void generDTO(List<FileOutConfig> focList, final GenerModel generModel, final PackageModel packageModel) {
        final String inputDtoPack = StrUtil.builder().append(packageModel.getParent()).append(StringPool.DOT).append(packageModel.getInputDto()).toString();
        final String outDtoPack = StrUtil.builder().append(packageModel.getParent()).append(StringPool.DOT).append(packageModel.getOutDto()).toString();
        final String dtoMapperDtoPack = StrUtil.builder().append(packageModel.getParent()).append(StringPool.DOT).append(packageModel.getDtoMapper()).toString();

        final String inputDtoUrl = StrUtil.builder().append(generModel.getPack()).append(StrUtil.replace(inputDtoPack, StringPool.DOT, StringPool.SLASH)).append(StringPool.SLASH).toString();
        final String outputDtoUrl = StrUtil.builder().append(generModel.getPack()).append(StrUtil.replace(outDtoPack, StringPool.DOT, StringPool.SLASH)).append(StringPool.SLASH).toString();
        final String dtoMapperUrl = StrUtil.builder().append(generModel.getPack()).append(StrUtil.replace(dtoMapperDtoPack, StringPool.DOT, StringPool.SLASH)).append(StringPool.SLASH).toString();
        focList.add(new FileOutConfig("/dto/create-input.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                log.info("generDTO -=- {}", JSON.toJSONString(tableInfo));
                return StrUtil.builder().append(inputDtoUrl).append(tableInfo.getEntityName()).append("CreateInputDTO.java").toString();
            }
        });
        focList.add(new FileOutConfig("/dto/update-input.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {

                return StrUtil.builder().append(inputDtoUrl).append(tableInfo.getEntityName()).append("UpdateInputDTO.java").toString();
            }
        });
        focList.add(new FileOutConfig("/dto/search-input.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return StrUtil.builder().append(inputDtoUrl).append(tableInfo.getEntityName()).append("SearchInputDTO.java").toString();
            }
        });
        focList.add(new FileOutConfig("/dto/list-output.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return StrUtil.builder().append(outputDtoUrl).append(tableInfo.getEntityName()).append("ListOutputDTO.java").toString();
            }
        });

        focList.add(new FileOutConfig("/dto/m-mapper.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return StrUtil.builder().append(dtoMapperUrl).append("/M").append(tableInfo.getEntityName()).append("Mapper.java").toString();
            }
        });

    }

}

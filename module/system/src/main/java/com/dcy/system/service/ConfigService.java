package com.dcy.system.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dcy.common.model.PageModel;
import com.dcy.db.base.service.BaseService;
import com.dcy.system.mapper.ConfigMapper;
import com.dcy.system.model.Config;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 参数配置表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2019-09-06
 */
@Service
public class ConfigService extends BaseService<ConfigMapper, Config> {

    /**
     * 根据配置key查询配置value
     *
     * @param key
     * @return
     */
    public String getValueByKey(String key) {
        String value = null;
        Config sysConfig = super.getOne(new LambdaQueryWrapper<Config>().eq(Config::getConfigKey, key));
        if (sysConfig != null) {
            value = sysConfig.getConfigValue();
        }
        return value;
    }

    /**
     * 分页查询
     *
     * @param config
     * @return
     */
    public IPage<Config> pageListByEntity(Config config, PageModel pageModel) {
        LambdaQueryWrapper<Config> queryWrapper = Wrappers.<Config>lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(config.getConfigName()), Config::getConfigName, config.getConfigName());
        return super.page(pageModel, queryWrapper);
    }
}

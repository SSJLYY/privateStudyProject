package com.dcy.system.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dcy.common.model.PageModel;
import com.dcy.db.base.service.BaseService;
import com.dcy.system.mapper.DictDataMapper;
import com.dcy.system.mapper.DictTypeMapper;
import com.dcy.system.model.DictData;
import com.dcy.system.model.DictType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 字典类型表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2021-03-17
 */
@Service
public class DictTypeService extends BaseService<DictTypeMapper, DictType> {

    @Autowired
    private DictDataMapper dictDataMapper;

    /**
     * 分页查询
     *
     * @param dictType
     * @return
     */
    public IPage<DictType> pageListByEntity(DictType dictType, PageModel pageModel) {
        LambdaQueryWrapper<DictType> queryWrapper = Wrappers.<DictType>lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(dictType.getDictName()), DictType::getDictName, dictType.getDictName());
        queryWrapper.like(StrUtil.isNotBlank(dictType.getDictType()), DictType::getDictType, dictType.getDictType());
        queryWrapper.eq(StrUtil.isNotBlank(dictType.getDictStatus()), DictType::getDictStatus, dictType.getDictStatus());
        return super.page(pageModel, queryWrapper);
    }

    /**
     * 删除类型 和 删除数据
     *
     * @param id
     * @return
     */
    public boolean deleteDictTypeById(String id) {
        final DictType dictType = getById(id);
        if (dictType != null) {
            dictDataMapper.delete(Wrappers.<DictData>lambdaQuery().eq(DictData::getDictType, dictType.getDictType()));
        }
        return removeById(id);
    }
}

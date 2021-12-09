package com.dcy.system.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dcy.common.model.PageModel;
import com.dcy.db.base.service.BaseService;
import com.dcy.system.enums.DictDataStatusEnum;
import com.dcy.system.mapper.DictDataMapper;
import com.dcy.system.model.DictData;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2021-03-17
 */
@Service
public class DictDataService extends BaseService<DictDataMapper, DictData> {

    /**
     * 获取表格数据
     *
     * @param dictData
     * @return
     */
    public IPage<DictData> pageListByEntity(DictData dictData, PageModel pageModel) {
        LambdaQueryWrapper<DictData> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(StrUtil.isNotBlank(dictData.getDictType()), DictData::getDictType, dictData.getDictType());
        queryWrapper.like(StrUtil.isNotBlank(dictData.getDictLabel()), DictData::getDictLabel, dictData.getDictLabel());
        queryWrapper.eq(StrUtil.isNotBlank(dictData.getDictStatus()), DictData::getDictStatus, dictData.getDictStatus());
        queryWrapper.orderByAsc(DictData::getDictValue);
        return super.page(pageModel, queryWrapper);
    }

    /**
     * 根据类型查询字典项
     *
     * @param dictType
     * @return
     */
    public List<DictData> getDictDataListByDictType(String dictType) {
        LambdaQueryWrapper<DictData> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DictData::getDictType, dictType);
        queryWrapper.eq(DictData::getDictStatus, DictDataStatusEnum.NORMAL.code);
        queryWrapper.orderByAsc(DictData::getDictSort);
        return list(queryWrapper);
    }
}

package com.dcy.db.base.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dcy.common.model.PageResult;
import com.dcy.common.model.R;
import com.dcy.db.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Author：dcy
 * @Description: 公共controller
 * @Date: 2019/9/6 13:19
 */
public abstract class BaseController<Service extends BaseService, Entity> extends RBaseController {

    @Autowired
    protected Service baseService;


    /**
     * 转换page
     *
     * @param iPage model分页数据
     * @param list  dto数据
     * @return
     */
    protected <DTO, T> PageResult<DTO> toPageDTO(IPage<T> iPage, List<DTO> list) {
        PageResult<DTO> pageResult = new PageResult<>();
        pageResult.setCurrent(iPage.getCurrent());
        pageResult.setPages(iPage.getPages());
        pageResult.setSize(iPage.getSize());
        pageResult.setTotal(iPage.getTotal());
        pageResult.setRecords(list);
        return pageResult;
    }

    /**
     * 查询全部
     *
     * @return
     */
    public R<List<Entity>> all() {
        return success(baseService.list());
    }


    /**
     * 根据对象id，查询详细信息
     *
     * @param id
     * @return
     */
    public R<Entity> getEntityById(Serializable id) {
        return (R<Entity>) success(baseService.getById(id));
    }

    /**
     * 添加
     *
     * @param entity
     * @return
     */
    public R<Boolean> save(Entity entity) {
        return success(baseService.save(entity));
    }

    /**
     * 修改
     *
     * @param entity
     * @return
     */
    public R<Boolean> update(Entity entity) {
        return success(baseService.updateById(entity));
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public R<Boolean> delete(Serializable id) {
        return success(baseService.removeById(id));
    }

    /**
     * 根据idList删除（对应的泛型是基本数据类型）
     *
     * @param idList
     * @return
     */
    public R<Boolean> deleteBatch(Collection<? extends Serializable> idList) {
        return success(baseService.removeByIds(idList));
    }
}

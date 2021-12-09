package com.dcy.system.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dcy.common.constant.RedisConstant;
import com.dcy.common.utils.TreeUtil;
import com.dcy.db.base.service.BaseService;
import com.dcy.system.mapper.DeptMapper;
import com.dcy.system.model.Dept;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2021-03-16
 */
@Service
public class DeptService extends BaseService<DeptMapper, Dept> {

    /**
     * 获取部门的tree数据
     *
     * @param dept
     * @return
     */
    public List<Dept> getDeptTreeList(Dept dept) {
        LambdaQueryWrapper<Dept> queryWrapper = Wrappers.<Dept>lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(dept.getName()), Dept::getName, dept.getName());
        queryWrapper.eq(StrUtil.isNotBlank(dept.getDeptStatus()), Dept::getDeptStatus, dept.getDeptStatus());
        queryWrapper.orderByAsc(Dept::getDeptSort);
        List<Dept> deptList = super.list(queryWrapper);
        return TreeUtil.listToTree(deptList, Comparator.comparing(Dept::getDeptSort));
    }

    @CacheEvict(value = {RedisConstant.REDIS_ROLE_DATA_SCOPE, RedisConstant.REDIS_ROLE_DATA_SCOPE_FLAG, RedisConstant.REDIS_USER_ROLE, RedisConstant.REDIS_USER_RESOURCE}, allEntries = true, condition = "#result == true")
    @Override
    public boolean save(Dept entity) {
        return super.save(entity);
    }

    @CacheEvict(value = {RedisConstant.REDIS_ROLE_DATA_SCOPE, RedisConstant.REDIS_ROLE_DATA_SCOPE_FLAG, RedisConstant.REDIS_USER_ROLE, RedisConstant.REDIS_USER_RESOURCE}, allEntries = true, condition = "#result == true")
    @Override
    public boolean updateById(Dept entity) {
        return super.updateById(entity);
    }

    @CacheEvict(value = {RedisConstant.REDIS_ROLE_DATA_SCOPE, RedisConstant.REDIS_ROLE_DATA_SCOPE_FLAG, RedisConstant.REDIS_USER_ROLE, RedisConstant.REDIS_USER_RESOURCE}, allEntries = true, condition = "#result == true")
    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }
}

package com.dcy.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dcy.common.constant.RedisConstant;
import com.dcy.common.utils.TreeUtil;
import com.dcy.db.base.service.BaseService;
import com.dcy.system.enums.ResourceStatusEnum;
import com.dcy.system.enums.ResourcesTypeEnum;
import com.dcy.system.mapper.ResourceMapper;
import com.dcy.system.mapper.RoleResMapper;
import com.dcy.system.mapper.UserInfoMapper;
import com.dcy.system.model.Resource;
import com.dcy.system.model.RoleRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2020-08-19
 */
@Service
public class ResourceService extends BaseService<ResourceMapper, Resource> {

    @Autowired
    private RoleResMapper roleResMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 获取tree-table 数据
     *
     * @return
     */
    public List<Resource> getResourceTreeList() {
        List<Resource> resourcesList = baseMapper.selectList(new LambdaQueryWrapper<Resource>().orderByAsc(Resource::getResSort));
        return TreeUtil.listToTree(resourcesList);
    }

    /**
     * 获取菜单列表
     *
     * @return
     */
    public List<Resource> getMenuList() {
        List<Resource> resourcesList = baseMapper.selectList(new LambdaQueryWrapper<Resource>()
                .in(Resource::getType, Arrays.asList(ResourcesTypeEnum.CATALOGUE.code, ResourcesTypeEnum.MENU.code))
                .orderByAsc(Resource::getResSort));
        return TreeUtil.listToTree(resourcesList);
    }

    /**
     * 获取tree 数据
     *
     * @param roleId
     * @return
     */
    public List<String> getResourceIdListByRoleId(String roleId) {
        LambdaQueryWrapper<RoleRes> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleRes::getRoleId, roleId);
        return roleResMapper.selectList(queryWrapper)
                .stream().map(RoleRes::getResId)
                .collect(Collectors.toList());
    }

    /**
     * 保存和修改
     *
     * @param entity
     * @return
     */
    @CacheEvict(value = {RedisConstant.REDIS_RESOURCE_ROUTER}, allEntries = true, condition = "#result == true")
    @Override
    public boolean saveOrUpdate(Resource entity) {
        return super.saveOrUpdate(entity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @CacheEvict(value = {RedisConstant.REDIS_RESOURCE_ROUTER}, allEntries = true, condition = "#result == true")
    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    /**
     * 获取路由表
     *
     * @return
     */
    @Cacheable(value = RedisConstant.REDIS_RESOURCE_ROUTER)
    public List<Resource> getRouterList() {
        return list(Wrappers.<Resource>lambdaQuery()
                .select(Resource::getPermission, Resource::getResPath, Resource::getHttpMethod)
                .isNotNull(Resource::getPermission)
                .isNotNull(Resource::getResPath)
                .isNotNull(Resource::getHttpMethod)
                .eq(Resource::getResStatus, ResourceStatusEnum.NORMAL.code)
                .orderByAsc(Resource::getResSort));
    }

    /**
     * 根据用户id 查询权限
     *
     * @param userId
     * @return
     */
    public List<Resource> listByUserId(String userId) {
        return TreeUtil.listToTree(userInfoMapper.selectAuthResourcesListByUserId(userId));
    }
}

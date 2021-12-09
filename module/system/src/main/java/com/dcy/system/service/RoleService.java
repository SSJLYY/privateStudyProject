package com.dcy.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.dcy.common.constant.RedisConstant;
import com.dcy.common.enums.RoleDataScopeEnum;
import com.dcy.common.model.PageModel;
import com.dcy.db.base.model.DataScopeRoleModel;
import com.dcy.db.base.service.BaseService;
import com.dcy.db.base.service.IDataScopeService;
import com.dcy.system.mapper.*;
import com.dcy.system.model.Role;
import com.dcy.system.model.RoleDept;
import com.dcy.system.model.RoleRes;
import com.dcy.system.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2020-08-19
 */
@Service
public class RoleService extends BaseService<RoleMapper, Role> implements IDataScopeService {

    @Autowired
    private RoleResMapper roleResMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private RoleDeptMapper roleDeptMapper;
    @Autowired
    private DeptMapper deptMapper;

    /**
     * 保存授权权限
     *
     * @param roleId 角色Id
     * @param resIds 授权权限Ids
     * @return
     */
    @CacheEvict(value = {RedisConstant.REDIS_USER_ROLE, RedisConstant.REDIS_USER_RESOURCE}, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveAuthResource(String roleId, List<String> resIds) {
        boolean success = false;
        if (StrUtil.isNotBlank(roleId) && resIds != null) {
            // 删除关联表
            roleResMapper.delete(new LambdaQueryWrapper<RoleRes>().eq(RoleRes::getRoleId, roleId));
            // 添加关联表
            List<RoleRes> roleRes = new ArrayList<>();
            resIds.forEach(resId -> roleRes.add(new RoleRes().setRoleId(roleId).setResId(resId)));
            SqlHelper.executeBatch(RoleRes.class, this.log, roleRes, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
                String sqlStatement = SqlHelper.getSqlStatement(RoleResMapper.class, SqlMethod.INSERT_ONE);
                sqlSession.insert(sqlStatement, entity);
            });
            success = true;
        }
        return success;
    }

    /**
     * 根据角色对象分页查询
     *
     * @param role
     * @return
     */
    public IPage<Role> pageListByEntity(Role role, PageModel pageModel) {
        LambdaQueryWrapper<Role> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(role.getRoleName()), Role::getRoleName, role.getRoleName());
        return super.page(pageModel, queryWrapper);
    }

    /**
     * 保存角色数据以及自定义权限
     *
     * @param role
     * @param deptIds
     * @return
     */
    @CacheEvict(value = {RedisConstant.REDIS_ROLE_DATA_SCOPE, RedisConstant.REDIS_ROLE_DATA_SCOPE_FLAG}, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRole(Role role, List<String> deptIds) {
        boolean success = saveOrUpdate(role);
        // 删除数据
        roleDeptMapper.delete(Wrappers.<RoleDept>lambdaQuery().eq(RoleDept::getRoleId, role.getId()));
        // 是否是自定义数据权限
        if (RoleDataScopeEnum.CUSTOM.code.equals(role.getDataScope()) && CollUtil.isNotEmpty(deptIds)) {
            // 添加数据
            List<RoleDept> roleDepts = new ArrayList<>();
            deptIds.forEach(s -> roleDepts.add(new RoleDept().setRoleId(role.getId()).setDeptId(s)));
            SqlHelper.executeBatch(RoleDept.class, this.log, roleDepts, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
                String sqlStatement = SqlHelper.getSqlStatement(RoleDeptMapper.class, SqlMethod.INSERT_ONE);
                sqlSession.insert(sqlStatement, entity);
            });
        }
        return success;
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @CacheEvict(value = {RedisConstant.REDIS_ROLE_DATA_SCOPE, RedisConstant.REDIS_ROLE_DATA_SCOPE_FLAG, RedisConstant.REDIS_USER_ROLE, RedisConstant.REDIS_USER_RESOURCE}, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(String id) {

        boolean success = removeById(id);
        // 删除数据
        roleDeptMapper.delete(Wrappers.<RoleDept>lambdaQuery().eq(RoleDept::getRoleId, id));
        return success;
    }

    /**
     * 批量删除角色
     *
     * @param idList
     * @return
     */
    @CacheEvict(value = {RedisConstant.REDIS_ROLE_DATA_SCOPE, RedisConstant.REDIS_ROLE_DATA_SCOPE_FLAG, RedisConstant.REDIS_USER_ROLE, RedisConstant.REDIS_USER_RESOURCE}, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatchRole(List<String> idList) {
        boolean success = removeByIds(idList);
        idList.forEach(s -> {
            // 删除数据
            roleDeptMapper.delete(Wrappers.<RoleDept>lambdaQuery().eq(RoleDept::getRoleId, s));
        });
        return success;
    }

    /**
     * 获取已授权的数据范围部门ids
     *
     * @param roleId
     * @return
     */
    public List<String> getDataScopeDeptIdsByRoleId(String roleId) {
        return roleDeptMapper.selectList(Wrappers.<RoleDept>lambdaQuery().eq(RoleDept::getRoleId, roleId)).stream().map(RoleDept::getDeptId).collect(Collectors.toList());
    }

    /**
     * 获取角色列表和是否有全部数据权限
     *
     * @param userId 用户id
     * @return
     */
    @Cacheable(value = RedisConstant.REDIS_ROLE_DATA_SCOPE_FLAG, key = "#userId")
    @Override
    public Map<String, Object> getAllDataScopeFlagAndDataByUserId(String userId) {
        // 设置数据范围类型
        final List<Role> roleList = userInfoMapper.selectAuthRoleListByUserId(userId);
        final boolean anyMatch = roleList.stream().anyMatch(role -> RoleDataScopeEnum.ALL.code.equals(role.getDataScope()));
        final Map<String, Object> map = new HashMap<>(2);
        // 角色列表
        map.put(IDataScopeService.ROLE_LIST, roleList);
        // 是否有全部数据权限
        map.put(IDataScopeService.ALL_DATA_SCOPE_FLAG, anyMatch);
        return map;
    }

    /**
     * 根据用户id和角色列表查询 获取所有的部门id
     *
     * @param userId   用户id
     * @param roleList 角色列表
     * @return
     */
    @Cacheable(value = RedisConstant.REDIS_ROLE_DATA_SCOPE, key = "#userId")
    @Transactional(readOnly = true)
    @Override
    public Set<String> getDateScopeDeptIds(final String userId, final List<DataScopeRoleModel> roleList) {
        Set<String> authDeptIds = new HashSet<>();
        roleList.forEach(role -> {
            // 获取枚举值
            final RoleDataScopeEnum dataScope = RoleDataScopeEnum.getByCode(role.getDataScope());
            if (dataScope != null) {
                switch (dataScope) {
                    case CUSTOM:
                        // 获取能看到的部门ids
                        final Set<String> seeDeptIds = baseMapper.selectAuthDeptIdsByRoleId(role.getId());
                        if (CollUtil.isNotEmpty(seeDeptIds)) {
                            authDeptIds.addAll(seeDeptIds);
                        }
                        break;
                    case ME_DEPT:
                        // 获取用户信息
                        UserInfo userInfo = userInfoMapper.selectById(userId);
                        authDeptIds.add(userInfo.getDeptId());
                        break;
                    case ME_DEPT_CHILD:
                        UserInfo userInfoOne = userInfoMapper.selectById(userId);
                        if (StrUtil.isNotBlank(userInfoOne.getDeptId())) {
                            authDeptIds.add(userInfoOne.getDeptId());
                            // 查询所有的子级
                            final Set<String> childrenIds = deptMapper.selectChildrenById(userInfoOne.getDeptId());
                            if (CollUtil.isNotEmpty(childrenIds)) {
                                authDeptIds.addAll(childrenIds);
                            }
                        }
                        break;
                    default:
                }
            }
        });
        return authDeptIds;
    }

}

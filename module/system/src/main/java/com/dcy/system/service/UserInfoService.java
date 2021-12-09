package com.dcy.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.dcy.common.constant.RedisConstant;
import com.dcy.common.model.PageModel;
import com.dcy.common.model.flowable.ActUserModel;
import com.dcy.common.service.ActUserService;
import com.dcy.db.base.enums.BaseModelDelFlagEnum;
import com.dcy.db.base.model.BaseModel;
import com.dcy.db.base.service.BaseService;
import com.dcy.system.mapper.UserInfoMapper;
import com.dcy.system.mapper.UserPostMapper;
import com.dcy.system.mapper.UserRoleMapper;
import com.dcy.system.model.Role;
import com.dcy.system.model.UserInfo;
import com.dcy.system.model.UserPost;
import com.dcy.system.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2020-08-19
 */
@Service
public class UserInfoService extends BaseService<UserInfoMapper, UserInfo> implements ActUserService {

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserPostMapper userPostMapper;

    /**
     * 根据用户名获取用户信息
     *
     * @param username
     * @return
     */
    public UserInfo getUserInfoByUsername(String username) {
        return super.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUsername, username).last("LIMIT 1"));
    }

    /**
     * 根据用户id 查询已授权角色列表
     *
     * @param userId
     * @return
     */
    public List<Role> getAuthRoleListByUserId(String userId) {
        return baseMapper.selectAuthRoleListByUserId(userId);
    }

    /**
     * 保存授权角色
     *
     * @param userId  用户id
     * @param roleIds 授权角色Ids
     * @return
     */
    @CacheEvict(value = {RedisConstant.REDIS_ROLE_DATA_SCOPE, RedisConstant.REDIS_ROLE_DATA_SCOPE_FLAG, RedisConstant.REDIS_USER_ROLE}, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveAuthRole(String userId, List<String> roleIds) {
        boolean success = false;
        if (StrUtil.isNotBlank(userId) && roleIds != null) {
            // 删除关联表
            userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
            // 添加关联表
            List<UserRole> userRoles = new ArrayList<>();
            roleIds.forEach(roleId -> userRoles.add(new UserRole().setUserId(userId).setRoleId(roleId)));
            SqlHelper.executeBatch(UserRole.class, this.log, userRoles, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
                String sqlStatement = SqlHelper.getSqlStatement(UserRoleMapper.class, SqlMethod.INSERT_ONE);
                sqlSession.insert(sqlStatement, entity);
            });
            success = true;
        }
        return success;
    }

    /**
     * 自定义sql分页查询
     *
     * @param userInfo
     * @return
     */
    public IPage<UserInfo> pageUserList(UserInfo userInfo, PageModel pageModel) {
        QueryWrapper<UserInfo> queryWrapper = Wrappers.<UserInfo>query();
        queryWrapper.eq("userInfo." + BaseModel.DEL_FLAG, BaseModelDelFlagEnum.NORMAL.code);
        queryWrapper.like(StrUtil.isNotBlank(userInfo.getUsername()), "userInfo." + UserInfo.USERNAME, userInfo.getUsername());
        queryWrapper.and(StrUtil.isNotBlank(userInfo.getDeptId()), userInfoQueryWrapper -> {
                    userInfoQueryWrapper.eq("userInfo." + UserInfo.DEPT_ID, userInfo.getDeptId())
                            .or().inSql("userInfo." + UserInfo.DEPT_ID, "SELECT sys_dept.id FROM sys_dept WHERE FIND_IN_SET('" + userInfo.getDeptId() + "', ancestors)");
                }
        );
        return baseMapper.selectPageList(getPagePlusInfo(pageModel), queryWrapper);
    }


    public IPage<UserInfo> pageList(UserInfo userInfo, PageModel pageModel) {
        QueryWrapper<UserInfo> queryWrapper = Wrappers.<UserInfo>query();
        queryWrapper.eq(BaseModel.DEL_FLAG, BaseModelDelFlagEnum.NORMAL.code);
        queryWrapper.like(StrUtil.isNotBlank(userInfo.getUsername()), UserInfo.USERNAME, userInfo.getUsername());
        queryWrapper.like(StrUtil.isNotBlank(userInfo.getNickName()), UserInfo.NICK_NAME, userInfo.getNickName());
        queryWrapper.and(StrUtil.isNotBlank(userInfo.getDeptId()), userInfoQueryWrapper -> {
                    userInfoQueryWrapper.eq(UserInfo.DEPT_ID, userInfo.getDeptId())
                            .or().inSql(UserInfo.DEPT_ID, "SELECT sys_dept.id FROM sys_dept WHERE FIND_IN_SET('" + userInfo.getDeptId() + "', ancestors)");
                }
        );
        return page(getPagePlusInfo(pageModel), queryWrapper);
    }

    /**
     * 保存用户和保存岗位
     *
     * @param userInfo
     * @param postIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUser(UserInfo userInfo, List<String> postIds) {
        boolean success = false;
        if (super.saveOrUpdate(userInfo)) {
            userPostMapper.delete(Wrappers.<UserPost>lambdaQuery().eq(UserPost::getUserId, userInfo.getId()));
            List<UserPost> userPosts = new ArrayList<>();
            postIds.forEach(s -> userPosts.add(new UserPost().setUserId(userInfo.getId()).setPostId(s)));
            SqlHelper.executeBatch(UserPost.class, this.log, userPosts, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
                String sqlStatement = SqlHelper.getSqlStatement(UserPostMapper.class, SqlMethod.INSERT_ONE);
                sqlSession.insert(sqlStatement, entity);
            });
            success = true;
        }
        return success;
    }

    /**
     * 根据用户id 查询岗位id
     *
     * @param userId
     * @return
     */
    public List<String> getPostListByUserId(String userId) {
        List<String> list = new ArrayList<>();
        List<UserPost> userPosts = userPostMapper.selectList(Wrappers.<UserPost>lambdaQuery().eq(UserPost::getUserId, userId));
        if (CollUtil.isNotEmpty(userPosts)) {
            list = userPosts.stream().map(UserPost::getPostId).collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public ActUserModel getActUserByUserId(String userId) {
        if (StrUtil.isBlank(userId)) {
            return null;
        }
        final UserInfo userInfo = getById(userId);
        if (userInfo == null) {
            return null;
        }
        return new ActUserModel().setUserId(userInfo.getId())
                .setUsername(userInfo.getUsername())
                .setNickName(userInfo.getNickName());
    }

    @Override
    public String getNickNameByUserId(String userId) {
        if (StrUtil.isBlank(userId)) {
            return null;
        }
        final UserInfo userInfo = getById(userId);
        if (userInfo == null) {
            return null;
        }
        return userInfo.getNickName();
    }

    @Override
    public List<String> getRoleIdsAndDeptId(String userId) {
        final UserInfo userInfo = getById(userId);
        final List<String> roleIds = userRoleMapper.selectList(Wrappers.<UserRole>lambdaQuery().select(UserRole::getRoleId).eq(UserRole::getUserId, userId))
                .stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<String> list = new ArrayList<>();
        if (userInfo != null && StrUtil.isNotBlank(userInfo.getDeptId())) {
            list.add(userInfo.getDeptId());
        }
        if (CollUtil.isNotEmpty(roleIds)) {
            list.addAll(roleIds);
        }
        return list;
    }
}

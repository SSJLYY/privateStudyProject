package com.dcy.modules.system.service;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import com.dcy.common.constant.Constant;
import com.dcy.common.constant.RedisConstant;
import com.dcy.modules.system.dto.output.LoginOutputDTO;
import com.dcy.system.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/4/2 15:14
 */
@Service
public class AuthService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 设置权限
     *
     * @param userId
     * @return
     */
    public Set<String> getAuthRoleAndResourceByUserId(String userId) {
        // 查询权限
        Set<String> resourcesCodeList = userInfoMapper.selectResourcesCodeByUserId(userId);
        Set<String> roleKeyList = userInfoMapper.selectAuthRoleKeyByUserId(userId);
        CollUtil.addAll(resourcesCodeList, roleKeyList);
        return resourcesCodeList;
    }

    /**
     * 根据用户id 刷新session里面的用户信息，只能修改登录人
     *
     * @param userId
     */
    public void refreshUserInfoByUserId(String userId) {
        SaSession session = StpUtil.getSessionByLoginId(userId, false);
        if (session != null) {
            Set<String> authRoleAndResourceByUserId = getAuthRoleAndResourceByUserId(userId);
            LoginOutputDTO loginOutputDTO = session.getModel(Constant.SESSION_USER_KEY, LoginOutputDTO.class);
            loginOutputDTO.setResources(authRoleAndResourceByUserId);
            session.set(Constant.SESSION_USER_KEY, loginOutputDTO);
        }
    }

    /**
     * 根据用户id查询权限集合
     *
     * @param userId
     * @return
     */
    @Cacheable(value = RedisConstant.REDIS_USER_RESOURCE, key = "#userId")
    public List<String> getResourcesCodeByUserId(String userId) {
        Set<String> resourcesCodeByUserId = userInfoMapper.selectResourcesCodeByUserId(userId);
        return CollUtil.newArrayList(resourcesCodeByUserId);
    }

    /**
     * 根据用户id查询角色集合
     *
     * @param userId
     * @return
     */
    @Cacheable(value = RedisConstant.REDIS_USER_ROLE, key = "#userId")
    public List<String> getAuthRoleKeyByUserId(String userId) {
        Set<String> authRoleKeyByUserId = userInfoMapper.selectAuthRoleKeyByUserId(userId);
        return CollUtil.newArrayList(authRoleKeyByUserId);
    }
}

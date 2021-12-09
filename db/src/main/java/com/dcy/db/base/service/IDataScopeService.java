package com.dcy.db.base.service;

import com.dcy.db.base.model.DataScopeRoleModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/9/3 14:24
 */
public interface IDataScopeService {

    /**
     * 是否有全部数据权限
     */
    String ALL_DATA_SCOPE_FLAG = "all-data-scope";

    /**
     * 角色列表
     */
    String ROLE_LIST = "role-list";

    /**
     * 获取角色列表和是否有全部数据权限
     *
     * @param userId 用户id
     * @return
     */
    Map<String, Object> getAllDataScopeFlagAndDataByUserId(String userId);


    /**
     * 根据用户id和角色列表查询 获取所有的部门id
     *
     * @param userId   用户id
     * @param roleList 角色列表
     * @return
     */
    Set<String> getDateScopeDeptIds(String userId, List<DataScopeRoleModel> roleList);
}

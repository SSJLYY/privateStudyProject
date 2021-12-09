package com.dcy.common.service;

import com.dcy.common.model.flowable.ActUserModel;

import java.util.List;

/**
 * @Author：dcy
 * @Description: 工作流获取用户service
 * @Date: 2021/6/11 9:09
 */
public interface ActUserService {


    /**
     * 根据用户id获取工作流用户对象
     *
     * @param userId 用户id
     * @return
     */
    ActUserModel getActUserByUserId(String userId);

    /**
     * 根据用户id查询昵称
     *
     * @param userId 用户id
     * @return
     */
    String getNickNameByUserId(String userId);


    /**
     * 根据用户id查询角色id和部门id
     *
     * @param userId 用户id
     * @return
     */
    List<String> getRoleIdsAndDeptId(String userId);
}

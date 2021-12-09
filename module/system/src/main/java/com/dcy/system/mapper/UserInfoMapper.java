package com.dcy.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.dcy.db.base.annotation.DataColumn;
import com.dcy.db.base.annotation.DataScope;
import com.dcy.system.model.Resource;
import com.dcy.system.model.Role;
import com.dcy.system.model.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author dcy
 * @since 2020-08-19
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     * 根据用户id 查询已授权角色列表
     *
     * @param userId 用户id
     * @return
     */
    List<Role> selectAuthRoleListByUserId(@Param("userId") String userId);

    /**
     * 根据用户id 查询已授权角色列表
     *
     * @param userId 用户id
     * @return
     */
    Set<String> selectAuthRoleKeyByUserId(@Param("userId") String userId);

    /**
     * 根据用户id 查询权限
     *
     * @param userId 用户id
     * @return
     */
    Set<String> selectResourcesCodeByUserId(@Param("userId") String userId);


    /**
     * 根据用户id 查询权限列表
     *
     * @param userId
     * @return
     */
    List<Resource> selectAuthResourcesListByUserId(@Param("userId") String userId);

    /**
     * 分页查询
     *
     * @param page    分页对象
     * @param wrapper 条件参数
     * @return
     */
    @DataScope(value = {
            @DataColumn(alias = "userInfo", name = "dept_id")
    })
    IPage<UserInfo> selectPageList(IPage<UserInfo> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}

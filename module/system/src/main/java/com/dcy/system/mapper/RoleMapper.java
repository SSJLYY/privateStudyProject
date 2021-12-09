package com.dcy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dcy.system.model.Resource;
import com.dcy.system.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author dcy
 * @since 2020-08-19
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据角色id 获取能看到的部门ids
     *
     * @param roleId 角色id
     * @return
     */
    Set<String> selectAuthDeptIdsByRoleId(@Param("roleId") String roleId);
}

package com.dcy.modules.system.dtomapper;

import com.dcy.modules.system.dto.input.RoleCreateInputDTO;
import com.dcy.modules.system.dto.input.RoleSearchInputDTO;
import com.dcy.modules.system.dto.input.RoleUpdateInputDTO;
import com.dcy.modules.system.dto.output.RoleListOutputDTO;
import com.dcy.system.model.Role;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/12/17 8:47
 */
@Mapper(componentModel = "spring")
public interface MRoleMapper {

    /**
     * 查询参数转换
     *
     * @param roleSearchInputDTO
     * @return
     */
    Role roleSearchInputDTOToRole(RoleSearchInputDTO roleSearchInputDTO);

    /**
     * role 转换 表格对象
     *
     * @param role
     * @return
     */
    RoleListOutputDTO roleToRoleListOutputDTO(Role role);

    /**
     * role 转换 转换 分页表格数据（多个对象）
     * 转换规则：自动寻找 对象找对象的方法进行循环
     * 所有如果需要List<> 转换 List 需要先写单对象转换  在写List转换的
     *
     * @param roles
     * @return
     */
    List<RoleListOutputDTO> rolesToRoleListOutputDTOs(List<Role> roles);

    /**
     * 创建角色DTO 转换 Role
     *
     * @param roleCreateInputDTO
     * @return
     */
    Role roleCreateInputDTOToRole(RoleCreateInputDTO roleCreateInputDTO);

    /**
     * 修改角色DTO 转换 Role
     *
     * @param roleUpdateInputDTO
     * @return
     */
    Role roleUpdateInputDTOToRole(RoleUpdateInputDTO roleUpdateInputDTO);
}


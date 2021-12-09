package com.dcy.modules.system.dtomapper;

import com.dcy.modules.system.dto.input.DeptCreateInputDTO;
import com.dcy.modules.system.dto.input.DeptSearchInputDTO;
import com.dcy.modules.system.dto.input.DeptUpdateInputDTO;
import com.dcy.modules.system.dto.output.DeptListOutputDTO;
import com.dcy.system.model.Dept;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/10/23 9:28
 */
@Mapper(componentModel = "spring")
public interface MDeptMapper {

    Dept toDept(DeptSearchInputDTO deptSearchInputDTO);

    DeptListOutputDTO toOutput(Dept dept);

    List<DeptListOutputDTO> toOutputList(List<Dept> depts);

    /**
     * 添加表单转换
     *
     * @param deptCreateInputDTO
     * @return
     */
    Dept toDept(DeptCreateInputDTO deptCreateInputDTO);

    /**
     * 修改表单转换
     *
     * @param deptUpdateInputDTO
     * @return
     */
    Dept toDept(DeptUpdateInputDTO deptUpdateInputDTO);
}

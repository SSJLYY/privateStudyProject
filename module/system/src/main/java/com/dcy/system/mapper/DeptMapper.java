package com.dcy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dcy.system.model.Dept;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author dcy
 * @since 2021-03-16
 */
public interface DeptMapper extends BaseMapper<Dept> {

    /**
     * 根据id 查询所有的子级
     *
     * @param id 部门id
     * @return
     */
    Set<String> selectChildrenById(@Param("id") String id);
}

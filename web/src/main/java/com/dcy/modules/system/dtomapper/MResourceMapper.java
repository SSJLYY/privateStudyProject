package com.dcy.modules.system.dtomapper;

import com.dcy.modules.system.dto.input.ResourceCreateInputDTO;
import com.dcy.modules.system.dto.input.ResourceUpdateInputDTO;
import com.dcy.modules.system.dto.output.ResourceListOutputDTO;
import com.dcy.system.model.Resource;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/10/23 15:03
 */
@Mapper(componentModel = "spring")
public interface MResourceMapper {

    /**
     * 创建DTO 转换 resource
     *
     * @param resourceCreateInputDTO
     * @return
     */
    Resource toResource(ResourceCreateInputDTO resourceCreateInputDTO);

    /**
     * 修改DTO 转换 resource
     *
     * @param resourceUpdateInputDTO
     * @return
     */
    Resource toResource(ResourceUpdateInputDTO resourceUpdateInputDTO);

    ResourceListOutputDTO toOutputDTO(Resource resource);

    List<ResourceListOutputDTO> toOutputDTO(List<Resource> resourceList);
}

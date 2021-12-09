package com.dcy.modules.system.dtomapper;

import com.dcy.modules.system.dto.input.DictTypeCreateInputDTO;
import com.dcy.modules.system.dto.input.DictTypeSearchInputDTO;
import com.dcy.modules.system.dto.input.DictTypeUpdateInputDTO;
import com.dcy.modules.system.dto.output.DictTypeListOutputDTO;
import com.dcy.system.model.DictType;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/10/23 9:28
 */
@Mapper(componentModel = "spring")
public interface MDictTypeMapper {

    DictType dictTypeSearchInputDTOToDictType(DictTypeSearchInputDTO dictTypeSearchInputDTO);

    DictType dictTypeCreateInputDTOToDictType(DictTypeCreateInputDTO dictTypeCreateInputDTO);

    DictType dictTypeUpdateInputDTOToDictType(DictTypeUpdateInputDTO dictTypeUpdateInputDTO);

    DictTypeListOutputDTO dictTypeToDictTypeListOutputDTO(DictType dictType);

    List<DictTypeListOutputDTO> dictTypesToDictTypeListOutputDTOs(List<DictType> dictTypes);

}

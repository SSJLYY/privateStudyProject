package com.dcy.modules.system.dtomapper;

import com.dcy.modules.system.dto.input.DictDataCreateInputDTO;
import com.dcy.modules.system.dto.input.DictDataSearchInputDTO;
import com.dcy.modules.system.dto.input.DictDataUpdateInputDTO;
import com.dcy.modules.system.dto.output.DictDataListOutputDTO;
import com.dcy.modules.system.dto.output.DictDataSelOutputDTO;
import com.dcy.modules.system.dto.output.DictDataTreeSelOutputDTO;
import com.dcy.system.model.DictData;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/10/23 9:28
 */
@Mapper(componentModel = "spring")
public interface MDictDataMapper {

    DictData dictDataSearchInputDTOToDictData(DictDataSearchInputDTO dictDataSearchInputDTO);

    DictData dictDataCreateInputDTOToDictData(DictDataCreateInputDTO dictDataCreateInputDTO);

    DictData dictDataUpdateInputDTOToDictData(DictDataUpdateInputDTO dictDataUpdateInputDTO);

    DictDataListOutputDTO dictDataToDictDataListOutputDTO(DictData dictData);

    List<DictDataListOutputDTO> dictDatasToDictDataListOutputDTOs(List<DictData> dictDatas);

    DictDataSelOutputDTO dictDataToDictDataSelOutputDTO(DictData dictData);

    List<DictDataSelOutputDTO> dictDatasToDictDataSelOutputDTOs(List<DictData> dictDatas);

    DictDataTreeSelOutputDTO dictDataToDictDataTreeSelOutputDTO(DictData dictData);

    List<DictDataTreeSelOutputDTO> dictDatasToDictDataTreeSelOutputDTOs(List<DictData> dictDatas);
}

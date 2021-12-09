package com.dcy.modules.system.dtomapper;

import com.dcy.modules.system.dto.input.ConfigCreateInputDTO;
import com.dcy.modules.system.dto.input.ConfigSearchInputDTO;
import com.dcy.modules.system.dto.input.ConfigUpdateInputDTO;
import com.dcy.modules.system.dto.output.ConfigListOutputDTO;
import com.dcy.system.model.Config;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/10/23 15:03
 */
@Mapper(componentModel = "spring")
public interface MConfigMapper {

    Config toConfig(ConfigCreateInputDTO configCreateInputDTO);

    Config toConfig(ConfigUpdateInputDTO configUpdateInputDTO);

    Config toConfig(ConfigSearchInputDTO configSearchInputDTO);

    ConfigListOutputDTO toOutput(Config config);

    List<ConfigListOutputDTO> toOutputList(List<Config> configs);
}

package ${cfg.dtoMapperPackage};

import ${cfg.inputDtoPackage}.${entity}CreateInputDTO;
import ${cfg.inputDtoPackage}.${entity}SearchInputDTO;
import ${cfg.inputDtoPackage}.${entity}UpdateInputDTO;
import ${cfg.outputDtoPackage}.${entity}ListOutputDTO;
import ${package.Entity}.${entity};
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author：${author}
 * @Description: ${table.comment!} 转换类
 * @Date: ${date}
 */
@Mapper(componentModel = "spring")
public interface M${entity}Mapper {

    ${entity} to${entity}(${entity}SearchInputDTO ${entity?uncap_first}SearchInputDTO);

    ${entity} to${entity}(${entity}CreateInputDTO ${entity?uncap_first}CreateInputDTO);

    ${entity} to${entity}(${entity}UpdateInputDTO ${entity?uncap_first}UpdateInputDTO);

    ${entity}ListOutputDTO toOutput(${entity} ${entity?uncap_first});

    List<${entity}ListOutputDTO> toOutputList(List<${entity}> ${entity?uncap_first}s);
}
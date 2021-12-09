package ${package.Controller};

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dcy.common.annotation.Log;
import com.dcy.common.model.R;
import com.dcy.common.model.PageModel;
import com.dcy.common.model.PageResult;
import ${cfg.inputDtoPackage}.${entity}CreateInputDTO;
import ${cfg.inputDtoPackage}.${entity}SearchInputDTO;
import ${cfg.inputDtoPackage}.${entity}UpdateInputDTO;
import ${cfg.outputDtoPackage}.${entity}ListOutputDTO;
import ${cfg.dtoMapperPackage}.M${entity}Mapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ${package.Entity}.${entity};
import ${package.ServiceImpl}.${table.serviceImplName};
import org.springframework.web.bind.annotation.*;
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@RequiredArgsConstructor
@Validated
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
@Api(value = "${table.controllerName}", tags = {"${table.comment!}操作接口"})
public class ${table.controllerName} extends ${superControllerClass}<${table.serviceImplName},${entity}> {
<#else>
public class ${table.controllerName} {
</#if>

    private final M${entity}Mapper m${entity}Mapper;

    @Log
    @ApiOperation(value = "${table.comment!}分页查询")
    @GetMapping("/page")
    public R<PageResult<${entity}ListOutputDTO>> pageList(${entity}SearchInputDTO ${entity?uncap_first}SearchInputDTO, PageModel pageModel) {
        // 转换model
        ${entity} ${entity?uncap_first} = m${entity}Mapper.to${entity}(${entity?uncap_first}SearchInputDTO);
        // 获取源对象
        IPage<${entity}> pageListByListInputDTO = baseService.pageListByEntity(${entity?uncap_first}, pageModel);
        // 转换新对象
        List<${entity}ListOutputDTO> ${entity?uncap_first}ListOutputDTOS = m${entity}Mapper.toOutputList(pageListByListInputDTO.getRecords());
        // 返回业务分页数据
        return success(toPageDTO(pageListByListInputDTO, ${entity?uncap_first}ListOutputDTOS));
    }

    @Log
    @ApiOperation(value = "添加")
    @PostMapping("/save")
    public R<Boolean> save(@Validated @ApiParam @RequestBody ${entity}CreateInputDTO ${entity?uncap_first}CreateInputDTO) {
        ${entity} ${entity?uncap_first} = m${entity}Mapper.to${entity}(${entity?uncap_first}CreateInputDTO);
        return super.save(${entity?uncap_first});
    }

    @Log
    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public R<Boolean> update(@Validated @ApiParam @RequestBody ${entity}UpdateInputDTO ${entity?uncap_first}UpdateInputDTO) {
        ${entity} ${entity?uncap_first} = m${entity}Mapper.to${entity}(${entity?uncap_first}UpdateInputDTO);
        return super.update(${entity?uncap_first});
    }

    @Log
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "id", value = "id", dataType = "String", paramType = "query", required = true)
    @PostMapping(value = "/delete")
    public R<Boolean> delete(@NotBlank(message = "id不能为空") @RequestParam String id) {
        return super.delete(id);
    }

    @Log
    @ApiOperation(value = "根据list删除")
    @PostMapping(value = "/deleteBatch")
    public R<Boolean> deleteBatch(@NotEmpty(message = "集合不能为空") @ApiParam @RequestBody List<String> idList) {
        return super.deleteBatch(idList);
    }

}
</#if>

package com.dcy.modules.system.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.dcy.common.annotation.Log;
import com.dcy.common.constant.Constant;
import com.dcy.common.model.R;
import com.dcy.db.base.controller.BaseController;
import com.dcy.modules.system.dto.input.ResourceCreateInputDTO;
import com.dcy.modules.system.dto.input.ResourceUpdateInputDTO;
import com.dcy.modules.system.dto.output.ResourceListOutputDTO;
import com.dcy.modules.system.dto.output.RouterListOutputDTO;
import com.dcy.modules.system.dtomapper.MResourceMapper;
import com.dcy.system.enums.ResourceMenuCacheFlagEnum;
import com.dcy.system.enums.ResourceMenuExtFlagEnum;
import com.dcy.system.enums.ResourceMenuHiddenFlagEnum;
import com.dcy.system.enums.ResourcesTypeEnum;
import com.dcy.system.model.Resource;
import com.dcy.system.service.ResourceService;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/8/26 9:31
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/system/resource")
@ApiSupport(order = 10)
@Api(value = "ResourceController", tags = {"资源接口"})
public class ResourceController extends BaseController<ResourceService, Resource> {

    private final MResourceMapper mResourcesMapper;

    @Log
    @ApiOperation(value = "获取资源tree列表数据")
    @GetMapping(value = "/getResourceTreeList")
    public R<List<ResourceListOutputDTO>> getResourceTreeList() {
        List<Resource> resourceTreeTableList = baseService.getResourceTreeList();
        List<ResourceListOutputDTO> resourcesListOutputDTOS = mResourcesMapper.toOutputDTO(resourceTreeTableList);
        return success(resourcesListOutputDTOS);
    }

    @Log
    @ApiOperation(value = "获取菜单列表数据")
    @GetMapping(value = "/getMenuList")
    public R<List<ResourceListOutputDTO>> getMenuList() {
        List<Resource> resourceTreeTableList = baseService.getMenuList();
        List<ResourceListOutputDTO> resourcesListOutputDTOS = mResourcesMapper.toOutputDTO(resourceTreeTableList);
        return success(resourcesListOutputDTOS);
    }

    @Log
    @ApiOperation(value = "根据角色id获取资源tree列表数据")
    @ApiImplicitParam(name = "id", value = "角色id", dataType = "roleId", paramType = "query", required = true)
    @GetMapping(value = "/getResourceIdListByRoleId")
    public R<List<String>> getResourceIdListByRoleId(@NotBlank(message = "角色id不能为空") @RequestParam String roleId) {
        return success(baseService.getResourceIdListByRoleId(roleId));
    }

    @Log
    @ApiOperation(value = "根据登录人获取目录和菜单路由（vue使用）")
    @GetMapping(value = "/getRouterList")
    public R<List<RouterListOutputDTO>> getResourceList() {
        final String userId = getUserId();
        final List<Resource> resourcesList = baseService.listByUserId(userId);
        return success(generateRouter(resourcesList));
    }

    private List<RouterListOutputDTO> generateRouter(List<Resource> resourcesList) {
        List<RouterListOutputDTO> routerListOutputDTOS = new ArrayList<>();
        resourcesList.forEach(resources -> {
            final List<Resource> children = resources.getChildren();
            RouterListOutputDTO routerListOutputDTO = new RouterListOutputDTO();
            routerListOutputDTO.setName(resources.getComponentName());
            routerListOutputDTO.setPath(resources.getRoutePath());
            routerListOutputDTO.setHidden(!ResourceMenuHiddenFlagEnum.YES.code.equals(resources.getMenuHiddenFlag()));
            if (!ResourceMenuExtFlagEnum.YES.code.equals(resources.getMenuExtFlag())) {
                // 一级菜单
                if (Constant.COMMON_PARENT_ID.equals(resources.getParentId())) {
                    routerListOutputDTO.setComponent(StrUtil.isBlank(resources.getComponentPath()) ? Constant.ROUTER_LAYOUT : resources.getComponentPath());
                    // 如果不是一级菜单，并且菜单类型为目录，则代表是多级菜单
                } else if (ResourcesTypeEnum.CATALOGUE.code.equals(resources.getType())) {
                    routerListOutputDTO.setComponent(StrUtil.isBlank(resources.getComponentPath()) ? Constant.ROUTER_PARENT_VIEW : resources.getComponentPath());
                } else if (StrUtil.isNotBlank(resources.getComponentPath())) {
                    routerListOutputDTO.setComponent(resources.getComponentPath());
                }
            }
            // 设置元信息
            final RouterListOutputDTO.MetaOutputDTO metaOutputDTO = new RouterListOutputDTO.MetaOutputDTO();
            metaOutputDTO.setIcon(resources.getMenuIcon());
            metaOutputDTO.setTitle(resources.getTitle());
            metaOutputDTO.setNoCache(ResourceMenuCacheFlagEnum.YES.code.equals(resources.getMenuCacheFlag()));
            routerListOutputDTO.setMeta(metaOutputDTO);
            if (CollUtil.isNotEmpty(children)) {
                routerListOutputDTO.setAlwaysShow(true);
                routerListOutputDTO.setRedirect(Constant.ROUTER_NO_REDIRECT);
                routerListOutputDTO.setChildren(generateRouter(children));
            } else if (Constant.COMMON_PARENT_ID.equals(resources.getParentId())) {
                RouterListOutputDTO routerListOutputDTONew = new RouterListOutputDTO();
                routerListOutputDTONew.setMeta(routerListOutputDTO.getMeta());
                // 不是外链
                if (!ResourceMenuExtFlagEnum.YES.code.equals(resources.getMenuExtFlag())) {
                    routerListOutputDTONew.setName(resources.getComponentName());
                    routerListOutputDTONew.setComponent(resources.getComponentPath());
                }
                routerListOutputDTONew.setPath(resources.getRoutePath());
                routerListOutputDTO.setAlwaysShow(true);
                routerListOutputDTO.setName(null);
                routerListOutputDTO.setComponent(Constant.ROUTER_LAYOUT);
                List<RouterListOutputDTO> listOutputDTOS = new ArrayList<>();
                listOutputDTOS.add(routerListOutputDTONew);
                routerListOutputDTO.setChildren(listOutputDTOS);
            }
            routerListOutputDTOS.add(routerListOutputDTO);
        });
        return routerListOutputDTOS;
    }

    @Log
    @ApiOperation(value = "添加")
    @PostMapping(value = "/save")
    public R<Boolean> save(@Validated @ApiParam @RequestBody ResourceCreateInputDTO resourcesCreateInputDTO) {
        Resource resources = mResourcesMapper.toResource(resourcesCreateInputDTO);
        resources.setCreateDate(new Date());
        return success(baseService.saveOrUpdate(resources));
    }

    @Log
    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public R<Boolean> update(@Validated @ApiParam @RequestBody ResourceUpdateInputDTO updateResourcesInputDTO) {
        Resource resources = mResourcesMapper.toResource(updateResourcesInputDTO);
        return success(baseService.saveOrUpdate(resources));
    }

    @Log
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "id", value = "资源id", dataType = "String", paramType = "query", required = true)
    @PostMapping(value = "/delete")
    public R<Boolean> delete(@NotBlank(message = "资源id不能为空") @RequestParam String id) {
        return success(baseService.removeById(id));
    }
}

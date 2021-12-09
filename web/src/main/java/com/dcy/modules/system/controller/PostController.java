package com.dcy.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dcy.common.annotation.Log;
import com.dcy.common.model.R;
import com.dcy.db.base.controller.BaseController;
import com.dcy.common.model.PageModel;
import com.dcy.common.model.PageResult;
import com.dcy.modules.system.dto.input.PostCreateInputDTO;
import com.dcy.modules.system.dto.input.PostSearchInputDTO;
import com.dcy.modules.system.dto.input.PostUpdateInputDTO;
import com.dcy.modules.system.dto.output.PostListOutputDTO;
import com.dcy.modules.system.dtomapper.MPostMapper;
import com.dcy.system.model.Post;
import com.dcy.system.service.PostService;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 岗位表 前端控制器
 * </p>
 *
 * @author dcy
 * @since 2021-03-16
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/system/post")
@ApiSupport(order = 30)
@Api(value = "PostController", tags = {"岗位管理接口"})
public class PostController extends BaseController<PostService, Post> {

    private final MPostMapper mPostMapper;

    @Log
    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public R<PageResult<PostListOutputDTO>> pageList(PostSearchInputDTO postSearchInputDTO, PageModel pageModel) {
        // 转换model
        Post post = mPostMapper.postSearchInputDTOToPost(postSearchInputDTO);
        // 获取源对象
        IPage<Post> pageListByListInputDTO = baseService.pageListByEntity(post,pageModel);
        // 转换新对象
        List<PostListOutputDTO> userInfoListOutputDTOS = mPostMapper.postsToPostListOutputDTOs(pageListByListInputDTO.getRecords());
        // 返回业务分页数据
        return success(toPageDTO(pageListByListInputDTO, userInfoListOutputDTOS));
    }

    @Log
    @ApiOperation(value = "获取所有数据")
    @GetMapping("/all")
    public R<List<PostListOutputDTO>> listAll() {
        List<Post> postList = baseService.list();
        // 转换新对象
        List<PostListOutputDTO> userInfoListOutputDTOS = mPostMapper.postsToPostListOutputDTOs(postList);
        // 返回业务分页数据
        return success(userInfoListOutputDTOS);
    }

    @Log
    @ApiOperation(value = "添加")
    @PostMapping("/save")
    public R<Boolean> save(@Validated @ApiParam @RequestBody PostCreateInputDTO postCreateInputDTO) {
        Post post = mPostMapper.postCreateInputDTOToPost(postCreateInputDTO);
        return super.save(post);
    }

    @Log
    @ApiOperation(value = "修改")
    @PostMapping(value = "/update")
    public R<Boolean> update(@Validated @ApiParam @RequestBody PostUpdateInputDTO postUpdateInputDTO) {
        Post post = mPostMapper.postUpdateInputDTOToPost(postUpdateInputDTO);
        return super.update(post);
    }

    @Log
    @ApiOperation(value = "删除")
    @ApiImplicitParam(name = "id", value = "岗位id", dataType = "String", paramType = "query", required = true)
    @PostMapping(value = "/delete")
    public R<Boolean> delete(@NotBlank(message = "岗位id不能为空") @RequestParam String id) {
        return super.delete(id);
    }

    @Log
    @ApiOperation(value = "根据list删除")
    @PostMapping(value = "/deleteBatch")
    public R<Boolean> deleteBatch(@NotEmpty(message = "集合不能为空") @ApiParam @RequestBody List<String> idList) {
        return super.deleteBatch(idList);
    }
}

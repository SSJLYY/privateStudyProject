package com.dcy.system.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dcy.common.model.PageModel;
import com.dcy.db.base.service.BaseService;
import com.dcy.system.mapper.PostMapper;
import com.dcy.system.model.Post;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 岗位表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2021-03-16
 */
@Service
public class PostService extends BaseService<PostMapper, Post> {

    /**
     * 表格查询
     *
     * @param post
     * @return
     */
    public IPage<Post> pageListByEntity(Post post, PageModel pageModel) {
        LambdaQueryWrapper<Post> queryWrapper = Wrappers.<Post>lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(post.getPostName()), Post::getPostName, post.getPostName());
        queryWrapper.eq(StrUtil.isNotBlank(post.getPostStatus()), Post::getPostStatus, post.getPostStatus());
        return super.page(pageModel, queryWrapper);
    }
}

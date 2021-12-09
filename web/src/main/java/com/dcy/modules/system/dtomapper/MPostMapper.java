package com.dcy.modules.system.dtomapper;

import com.dcy.modules.system.dto.input.PostCreateInputDTO;
import com.dcy.modules.system.dto.input.PostSearchInputDTO;
import com.dcy.modules.system.dto.input.PostUpdateInputDTO;
import com.dcy.modules.system.dto.output.PostListOutputDTO;
import com.dcy.system.model.Post;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/10/23 9:28
 */
@Mapper(componentModel = "spring")
public interface MPostMapper {


    Post postSearchInputDTOToPost(PostSearchInputDTO postSearchInputDTO);

    PostListOutputDTO postToPostListOutputDTO(Post post);

    List<PostListOutputDTO> postsToPostListOutputDTOs(List<Post> posts);

    /**
     * 添加表单转换
     *
     * @param postCreateInputDTO
     * @return
     */
    Post postCreateInputDTOToPost(PostCreateInputDTO postCreateInputDTO);

    /**
     * 修改表单转换
     *
     * @param postUpdateInputDTO
     * @return
     */
    Post postUpdateInputDTOToPost(PostUpdateInputDTO postUpdateInputDTO);
}

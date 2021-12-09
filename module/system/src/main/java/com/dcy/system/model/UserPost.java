package com.dcy.system.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户与岗位关联表
 * </p>
 *
 * @author dcy
 * @since 2021-03-17
 */
@Data
@Accessors(chain = true)
@TableName("sys_user_post")
public class UserPost implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 岗位id
     */
    private String postId;


    public static final String USER_ID = "user_id";

    public static final String POST_ID = "post_id";

}

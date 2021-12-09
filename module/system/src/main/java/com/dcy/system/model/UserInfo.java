package com.dcy.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dcy.db.base.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author dcy
 * @since 2020-08-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_info")
public class UserInfo extends BaseModel {

    /**
     * 用户id
     */
    private String id;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户类型（0、管理员；1、普通用户）
     */
    private String userType;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 性别（0、男；1、女）
     */
    private String sex;

    /**
     * 头像
     */
    private String avatarPath;

    /**
     * 帐号状态（0、正常；1、禁用）
     *
     * @see com.dcy.system.enums.UserInfoStatusEnum
     */
    private String userStatus;

    /**
     * 部门名称
     */
    @TableField(exist = false)
    private String deptName;

    public static final String ID = "id";

    public static final String USERNAME = "username";

    public static final String DEPT_ID = "dept_id";

    public static final String PASSWORD = "password";

    public static final String NICK_NAME = "nick_name";

    public static final String USER_TYPE = "user_type";

    public static final String EMAIL = "email";

    public static final String PHONE_NUMBER = "phone_number";

    public static final String SEX = "sex";

    public static final String AVATAR_PATH = "avatar_path";

    public static final String USER_STATUS = "user_status";

}

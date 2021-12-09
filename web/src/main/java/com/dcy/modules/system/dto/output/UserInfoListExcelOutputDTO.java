package com.dcy.modules.system.dto.output;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/10/23 9:18
 */
@Getter
@Setter
@ToString
public class UserInfoListExcelOutputDTO {

    @Excel(name = "用户名")
    private String username;

    @Excel(name = "用户昵称")
    private String nickName;

    @Excel(name = "用户邮箱")
    private String email;

    @Excel(name = "手机号码")
    private String phoneNumber;

    @Excel(name = "性别", replace = {"男_0", "女_1"}, suffix = "生")
    private String sex;

    @Excel(name = "帐号状态", replace = {"正常_0", "禁用_1"})
    private String userStatus;

    @Excel(name = "创建时间", format = "yyyy-MM-dd")
    private Date createDate;
}

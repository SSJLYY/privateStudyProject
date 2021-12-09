package com.dcy.modules.business.controller;

import com.dcy.common.model.R;
import com.dcy.modules.business.model.UserVal;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/5/30 9:25
 */
@Validated
@Slf4j
@RestController
@RequestMapping("/system/user2")
@ApiSupport(order = 2)
@Api(value = "UserController2", tags = {"校验参数接口测试"})
public class UserController2 {

    @ApiOperation(value = "测试校验嵌套对象")
    @ApiOperationSupport(order = 7, author = "dcy")
    @PostMapping("/testValidated2")
    public R<String> testValidated2(@Validated @ApiParam @RequestBody UserVal userVal) {
        log.info("testValidated2 {}", userVal);
        return R.success("校验成功");
    }

    @ApiOperation(value = "测试校验基本数据类型")
    @ApiOperationSupport(order = 7, author = "dcy")
    @ApiImplicitParam(name = "id", value = "用户id", dataType = "String", paramType = "query")
    @PostMapping("/testValidated3")
    public R<String> testValidated3(@NotBlank(message = "用户id不能为空") @RequestParam String id) {
        log.info("id {}", id);
        return R.success("校验成功");
    }

    @ApiOperation(value = "测试校验基本数据类型2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "名称", dataType = "String", paramType = "query")
    })
    @ApiOperationSupport(order = 8, author = "dcy")
    @PostMapping("/testValidated4")
    public R<String> testValidated4(@NotBlank(message = "用户id不能为空") String id, @NotBlank(message = "名称不能为空") String name) {
        log.info("id {}", id);
        log.info("name {}", name);
        return R.success("校验成功");
    }
}

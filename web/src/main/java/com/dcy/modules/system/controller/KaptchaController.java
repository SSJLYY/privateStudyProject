package com.dcy.modules.system.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.io.IoUtil;
import com.dcy.common.annotation.Log;
import com.dcy.common.constant.Constant;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/1/6 9:30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/kaptcha")
@ApiSupport(order = 800)
@Api(value = "KaptchaController", tags = {"验证码接口暂时不用"})
@Slf4j
public class KaptchaController {

    private final RedisTemplate<String, String> redisTemplate;

    @Log
    @ApiOperation(value = "生成验证码")
    @ApiImplicitParam(name = "uuid", value = "客户端id", dataType = "String", paramType = "query", required = true)
    @GetMapping(value = "/render")
    public void render(String uuid, HttpServletResponse response) throws IOException {
        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(200, 50, 4, 4);
        shearCaptcha.write(response.getOutputStream());
        log.info("验证码 [{}]", shearCaptcha.getCode());
        // 验证码过期时间一分钟
        redisTemplate.opsForValue().set(Constant.REDIS_CAPTCHA_KEY + uuid, shearCaptcha.getCode(), 1, TimeUnit.MINUTES);
        // 校验验证码
        boolean verify = shearCaptcha.verify(shearCaptcha.getCode());
        log.info("验证码结果 [{}]", shearCaptcha.verify("1234"));
        log.info("验证码结果 [{}]", verify);
        // 关闭流
        String code = redisTemplate.opsForValue().get(Constant.REDIS_CAPTCHA_KEY + uuid);
        log.info("验证码结果 redis [{}]", code);
        IoUtil.close(response.getOutputStream());
    }

}

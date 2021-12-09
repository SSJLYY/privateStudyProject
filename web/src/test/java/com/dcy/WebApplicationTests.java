package com.dcy;

import com.dcy.system.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
@SpringBootTest
class WebApplicationTests {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void contextLoads() {

    }

    @Test
    void testRedisList() {
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName("测试名称");
        Long aLong = redisTemplate.opsForList().rightPush("user-list", userInfo);
        log.info(" testRedisList {}", aLong);

        UserInfo o = (UserInfo) redisTemplate.opsForList().leftPop("user-list");
        log.info(" userinfo {}", o);
    }
}

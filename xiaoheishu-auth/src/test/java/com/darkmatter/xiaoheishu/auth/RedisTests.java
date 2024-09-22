package com.darkmatter.xiaoheishu.auth;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
@Slf4j
public class RedisTests {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     *  set key value
     */
    @Test
    public void set() {
        redisTemplate.opsForValue().set("name", "xiaoheishu");
    }
}

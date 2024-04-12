package org.czg.redis.center.biz.cache.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
@Service
public class DemoService {

    private final RedisTemplate<String, Object> redisTemplate;

    public String getValue(String key) {
        Object o = redisTemplate.opsForValue().get(key);
        return o == null ? null : o.toString();
    }

    public void setValue(String key, Object value, Long ttl) {
        if (null == ttl) {
            redisTemplate.opsForValue().set(key, value);
        } else {
            redisTemplate.opsForValue().set(key, value, ttl, TimeUnit.MILLISECONDS);
        }
    }

}

package org.czg.redis.center.biz.lock.ordinary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Duration;

/**
 * @author czg
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrdinaryLockService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void lock(String key, String value, Long expire) {
        Boolean b = redisTemplate.opsForValue().setIfAbsent(key, value, Duration.ofSeconds(expire));
        Assert.isTrue(Boolean.TRUE.equals(b), "获取锁失败，请稍后重试");
    }

    public void unlock(String key) {
        Object o = redisTemplate.opsForValue().get(key);
        if (o == null) {
            log.info("锁[{}]不存在", key);
            return;
        }
        redisTemplate.delete(key);
        log.info("解锁[{}]成功", key);
    }

}

package org.czg.redis.center.biz.lock.id.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author czg
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UniqueIdService {

    private final RedisTemplate<String, Object> redisTemplate;

    public Long getUniqueId(String key) {
        return redisTemplate.opsForValue().increment(key, 1);
    }

}

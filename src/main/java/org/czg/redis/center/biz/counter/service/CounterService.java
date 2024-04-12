package org.czg.redis.center.biz.counter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.counter.enums.BehaviorEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author czg
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class CounterService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final static String COUNTER_KEY = "counter:%s:%s";

    public void incrBehavior(String event, String behavior) {
        validBehavior(behavior);
        redisTemplate.opsForValue().increment(String.format(COUNTER_KEY, event, behavior));
    }

    public void decrBehavior(String event, String behavior) {
        validBehavior(behavior);
        Long behaviorCounter = getBehaviorCounter(event, behavior);
        if (behaviorCounter > 0) {
            redisTemplate.opsForValue().decrement(String.format(COUNTER_KEY, event, behavior));
        }
    }

    public Long getBehaviorCounter(String event, String behavior) {
        validBehavior(behavior);
        Object o = redisTemplate.opsForValue().get(String.format(COUNTER_KEY, event, behavior));
        if (null == o) {
            return 0L;
        }
        return Long.parseLong(o.toString());
    }

    private void validBehavior(String behavior) {
        Assert.notNull(BehaviorEnum.of(behavior), "暂不支持该行为");
    }

}

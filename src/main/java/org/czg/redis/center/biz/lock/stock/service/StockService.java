package org.czg.redis.center.biz.lock.stock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author czg
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void decrementStock(String key, Long quantity) {
        Long stock = redisTemplate.opsForValue().decrement(key, quantity);
        Assert.isTrue(null != stock && stock >= 0, "库存不足");
    }

}

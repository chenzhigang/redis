package org.czg.redis.center.biz.message.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.message.contants.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * @author czg
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DelayMessageService {

    private final ZSetOperations<String, Object> zSetOperations;

    private final StringRedisTemplate stringRedisTemplate;

    private final static String ORDER_PREFIX = "0";

    private final static String ORDER_ID_INCREMENT_KEY = String.format("%s:sequence", Constants.ORDER_DELAY_PREFIX);

    private final static String ORDER_KEY = String.format("%s:%%s", Constants.ORDER_DELAY_PREFIX);

    /**
     * 延迟时间（单位：秒）
     */
    @Value("${order.delay.time:900}")
    private Long delayTime;

    public void addMessage() {
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        // 订单id自增
        Long increment = getOrderId();
        // 生成订单id
        String orderNo = genOrderNo(now, increment);
        // 保存订单 TODO
        // 设置订单
        String orderKey = getOrderKey(increment);
        stringRedisTemplate.opsForValue().set(orderKey, String.valueOf(increment));
        // 设置过期时间
        stringRedisTemplate.expire(orderKey, Duration.ofSeconds(delayTime));
    }

    private String genOrderNo(LocalDateTime now, Long increment) {
        String dateStr = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return String.format("%s%s%s", ORDER_PREFIX, dateStr, String.format("%05d", increment));
    }

    public String getOrderKey(Long orderId) {
        return String.format(ORDER_KEY, orderId);
    }

    public Long getOrderId() {
        return stringRedisTemplate.opsForValue().increment(ORDER_ID_INCREMENT_KEY, 1);
    }

    private final static String PUSH_DELAY_KEY = "order:delay:zset";

    public void pushDelayMessage(String content) {
        zSetOperations.add(PUSH_DELAY_KEY, content, System.currentTimeMillis() + delayTime * 1000);
    }

    public Set<Object> popDelayMessage(double min, double max) {
        return zSetOperations.rangeByScore(PUSH_DELAY_KEY, min, max);
    }

    public void removeDelayMessage(Object o) {
        zSetOperations.remove(PUSH_DELAY_KEY, o);
    }

    public void removeDelayMessage(double min, double max) {
        zSetOperations.removeRangeByScore(PUSH_DELAY_KEY, min, max);
    }
}

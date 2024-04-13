package org.czg.redis.center.biz.message.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * @author czg
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(String channel, String message) {
        redisTemplate.convertAndSend(channel, message);
    }

    public void subscribe(String channel) {
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.subscribe((message, pattern) -> {
                String receiveMessage = new String(message.getBody());
                log.info("接收到消息:{}", receiveMessage);
            }, channel.getBytes(StandardCharsets.UTF_8));
            return null;
        });
    }

}

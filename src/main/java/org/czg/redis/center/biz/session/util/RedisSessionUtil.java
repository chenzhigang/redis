package org.czg.redis.center.biz.session.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author czg
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RedisSessionUtil {

    private final SessionRepository<? extends Session> sessionRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.session.redis.namespace:spring:session}")
    private String defaultNamespace;

    /**
     * session过期时间，默认30分钟=1800秒，单位默认秒
     */
    @Value("${spring.session.timeout:1800}")
    private Duration sessionTimeOutDuration;

    public String getRedisKey(String sessionId) {
        return String.format("%s:sessions:%s", defaultNamespace, sessionId);
    }

    public void setSessionExpire(String sessionId, Long ttl) {
        Session session = sessionRepository.findById(sessionId);
        if (null == session) {
            throw new BusinessException("sessionId不存在");
        }
        ttl = null == ttl ? sessionTimeOutDuration.getSeconds() : ttl;
        String redisKey = getRedisKey(sessionId);
        // key是否存在
        boolean exists = Boolean.TRUE.equals(redisTemplate.hasKey(redisKey));
        if (exists) {
            // 删除旧的过期时间
            redisTemplate.persist(redisKey);
            // 设置新的过期时间
            Boolean flag = redisTemplate.expire(redisKey, ttl, TimeUnit.SECONDS);
            log.info("设置[{}]过期时间：{}", sessionId, flag);
        }
    }

}

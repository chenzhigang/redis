package org.czg.redis.center.biz.ratelimiter.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author czg
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RateLimiterService {

    private final RedisTemplate<String, Object> redisTemplate;

    private DefaultRedisScript<String> script;

    private static final String KEY = "requestUri:%s";

    /**
     * 在60秒内允许请求100次
     */
    private static final Integer RATE_LIMIER_COUNT = 100;

    /**
     * 统计60秒内的限流情况
     */
    private static final Long RATE_LIMITER_TIME = 60L;

    @PostConstruct
    public void init() {
        script = new DefaultRedisScript<>();
        script.setResultType(String.class);
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/rateLimiter.lua")));
    }

    public void rateLimiter(String requestId) {
        for (int i = 0; i < 500; i++) {
            CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("系统异常", e);
                }
                List<String> keys = new ArrayList<>(Collections.singleton(String.format(KEY, requestId)));
                String execute = redisTemplate.execute(script, keys,
                        String.valueOf(RATE_LIMIER_COUNT),
                        String.valueOf(RATE_LIMITER_TIME),
                        String.valueOf(System.currentTimeMillis() / 1000),
                        UUID.randomUUID().toString());
                Assert.isTrue(StringUtils.isBlank(execute), execute);
                return null;
            }).exceptionally(ex -> {
                log.error("执行失败", ex);
                return null;
            });
        }

    }

}

package org.czg.redis.center.biz.lock.lua.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author czg
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class LuaLockService {

    private final RedisTemplate<String, Object> redisTemplate;

    private DefaultRedisScript<Boolean> lockScript;

    private DefaultRedisScript<Boolean> unlockScript;

    private DefaultRedisScript<Boolean> unlockForValueScript;

    @PostConstruct
    public void init() {
        // 加锁脚本初始化
        lockScript = new DefaultRedisScript<>();
        // lua加锁脚本返回类型
        lockScript.setResultType(Boolean.class);
        // 加载lua加锁脚本
        lockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/lock.lua")));

        // 解锁脚本初始化
        unlockScript = new DefaultRedisScript<>();
        // lua解锁脚本返回类型
        unlockScript.setResultType(Boolean.class);
        // 加载lua解锁脚本
        unlockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/unlock.lua")));

        // 解锁脚本初始化（匹配值）
        unlockForValueScript = new DefaultRedisScript<>();
        // lua解锁脚本返回类型
        unlockForValueScript.setResultType(Boolean.class);
        // 加载lua解锁脚本
        unlockForValueScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/unlockForValue.lua")));
    }

    public boolean lock(String key, String value, Long expire) {
        log.info("加锁[键：{}，值：{}，过期时间：{}]", key, value, expire);
        List<String> keys = new ArrayList<>(Collections.singletonList(key));
        Boolean execute = redisTemplate.execute(lockScript, keys, value, expire.toString());
        return Boolean.TRUE.equals(execute);
    }

    public boolean lockForRetry(String key, String value, Long expire, Integer retryTimes) {
        log.info("加锁[键：{}，值：{}，过期时间：{}，重试次数：{}]", key, value, expire, retryTimes);
        List<String> keys = new ArrayList<>(Collections.singletonList(key));
        Boolean execute = redisTemplate.execute(lockScript, keys, value, expire.toString());
        if (Boolean.TRUE.equals(execute)) {
            log.info("加锁成功[键：{}，值：{}]", key, value);
            return true;
        }
        if (null == retryTimes || retryTimes <= 0) {
            log.info("加锁失败");
            return false;
        }
        int retryCount = 1;
        while (true) {
            try {
                log.info("尝试加锁[键：{}，值：{}，加锁次数：{}]", key, value, retryCount);
                execute = redisTemplate.execute(lockScript, keys, value, expire.toString());
                retryCount++;
                if (Boolean.TRUE.equals(execute)) {
                    log.info("加锁成功[键：{}，值：{}，加锁次数：{}]", key, value, retryCount);
                    break;
                }
                if (retryCount >= retryTimes) {
                    log.error("加锁失败[键：{}，加锁次数：{}]", key, retryCount);
                    retryCount++;
                    break;
                }
            } catch (Exception e) {
                log.error("加锁失败[键：{}，加锁次数：{}]", key, retryCount, e);
            }
        }
        if (retryCount > retryTimes) {
            log.info("加锁失败");
            return false;
        }
        return true;
    }

    public void unlock(String key) {
        log.info("解锁[键：{}]", key);
        List<String> keys = new ArrayList<>(Collections.singletonList(key));
        Boolean execute = redisTemplate.execute(unlockScript, keys);
        Assert.isTrue(Boolean.TRUE.equals(execute), "解锁失败");
    }

    public void unlockForValue(String key, String value) {
        log.info("解锁[键：{}，值：{}]", key, value);
        List<String> keys = new ArrayList<>(Collections.singletonList(key));
        Boolean execute = redisTemplate.execute(unlockForValueScript, keys, value);
        Assert.isTrue(Boolean.TRUE.equals(execute), "解锁失败");
    }

}

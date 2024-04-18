package org.czg.redis.center.biz.list.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author czg
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LatestListService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final static String PUSH_KEY_STR = "list:push";

    public void push(String value) {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        listOperations.leftPush(PUSH_KEY_STR, value);
        // 限制最多显示最新的数据数量
        listOperations.trim(PUSH_KEY_STR, 0, 99);
    }

    public List<Object> getLatestList(int count) {
        return redisTemplate.opsForList().range(PUSH_KEY_STR, 0, count);
    }

}

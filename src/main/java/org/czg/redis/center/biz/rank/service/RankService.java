package org.czg.redis.center.biz.rank.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.rank.model.Rank;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author czg
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RankService {

    private final RedisTemplate<String, String> redisTemplate;

    public void addToRanking(String username, int score) {
        redisTemplate.opsForZSet().add("ranking", username, score);
        redisTemplate.expire("ranking", 86400, TimeUnit.SECONDS);
    }

    public List<Rank> getRanking() {
        List<Rank> rankingInfos = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<String>> rankingSet = redisTemplate.opsForZSet().reverseRangeWithScores("ranking", 0, -1);
        assert rankingSet != null;
        for (ZSetOperations.TypedTuple<String> tuple : rankingSet) {
            Rank rank = new Rank();
            rank.setUsername(tuple.getValue());
            rank.setScore(Objects.requireNonNull(tuple.getScore()).intValue());
            rankingInfos.add(rank);
            log.info("username: {}, score: {}", tuple.getValue(), tuple.getScore().intValue());
        }
        return rankingInfos;
    }
}
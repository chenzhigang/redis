package org.czg.redis.center.biz.lottery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author czg
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LotteryService {

    private final StringRedisTemplate stringRedisTemplate;

    private final static String LOTTERY_STR = "lottery:participants";

    public void joinLottery(String participant) {
        stringRedisTemplate.opsForSet().add(LOTTERY_STR, participant);
    }

    public String drawWinner() {
        return stringRedisTemplate.opsForSet().pop(LOTTERY_STR);
    }

    public List<String> drawWinner(int participantCount) {
        return stringRedisTemplate.opsForSet().pop(LOTTERY_STR, participantCount);
    }

}

package org.czg.redis.center.biz.lock.seckill.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.czg.redis.center.biz.lock.seckill.model.GoodsStockParam;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author czg
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SeckillService {

    private final RedisTemplate<String, Object> redisTemplate;

    private DefaultRedisScript<String> script;

    @PostConstruct
    public void init() {
        script = new DefaultRedisScript<>();
        script.setResultType(String.class);
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/seckill.lua")));
    }

    public String seckill(String key, Long amount, Integer index) {
        List<String> keys = new ArrayList<>(Collections.singletonList(key));
        return redisTemplate.execute(script, keys, amount.toString(), index.toString());
    }

    public void initStock(GoodsStockParam goodsStockParam) {
        redisTemplate.opsForValue().set(goodsStockParam.getGoodsInfo(), goodsStockParam.getStock().toString());
    }

    public List<String> seckillFast(String goodsInfo) {
        List<String> resultList = new ArrayList<>();
        List<CompletableFuture<String>> cList = new ArrayList<>();
        List<String> successedList = new ArrayList<>();
        List<String> failedList = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(150);
        for (int i = 1; i <= 150; i++) {
            try {
                final int index = i;
                CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> {
                    long amount = 1 + (long) (Math.random() * 10);
                    String msg = seckill(goodsInfo, amount, index);
                    log.info("{}[请求：{}，数量：{}]", msg, index, amount);
                    return msg;
                }).exceptionally(ex -> {
                    log.error("秒杀失败[{}]", index, ex);
                    return index + ":秒杀失败";
                });
                cList.add(result);
            } finally {
                countDownLatch.countDown();
            }
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("系统异常", e);
        }
        cList.forEach(it -> {
            try {
                resultList.add(it.get());
            } catch (Exception e) {
                log.error("获取数据失败", e);
            }
        });
        resultList.forEach(it -> {
            if (it.contains("秒杀成功")) {
                successedList.add(it);
            } else {
                failedList.add(it);
            }
        });
        log.info("秒杀成功数组：{}，总数量：{}", successedList, successedList.size());
        log.info("秒杀失败数组：{}，总数量：{}", failedList, failedList.size());
        return resultList;
    }
}

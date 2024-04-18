package org.czg.redis.center.biz.stream.seckill.service;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.czg.redis.center.biz.lock.lua.service.LuaLockService;
import org.czg.redis.center.biz.stream.seckill.model.SeckillModel;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author czg
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class StreamSeckillService {

    private final StringRedisTemplate stringRedisTemplate;

    private DefaultRedisScript<Long> script;

    private final LuaLockService luaLockService;

    @Lazy
    @Resource
    private StreamSeckillService streamSeckillService;

    private final static String GROUP = "seckill_group";

    private final static String NAME = "seckill_name";

    private final static ExecutorService EXECUTE = Executors.newSingleThreadExecutor();

    private final static String SECKILL_KEY = "order:seckill";

    @PostConstruct
    public void init() {
        // 秒杀lua脚本初始化
        script = new DefaultRedisScript<>();
        script.setResultType(Long.class);
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/streamSeckill.lua")));

        // 先注释掉，后期要用可以再开
        EXECUTE.submit(new SeckillRunnable());
    }

    private class SeckillRunnable implements Runnable {
        @Override
        public void run() {
            log.info("开始监听下单消息……");
            while (true) {
                try {
                    Long keyCount = stringRedisTemplate.countExistingKeys(Collections.singleton(SECKILL_KEY));
                    if (null == keyCount || keyCount == 0) {
                        log.info("该键[{}]不存在", SECKILL_KEY);
                        // 休眠2秒等待key创建
                        Thread.sleep(2000);
                        continue;
                    }

                    // 获取消息队列中的订单信息 XREADGROUP GROUP g1 c1 COUNT 1 BLOCK 2000 STREAMS s1 >
                    List<MapRecord<String, Object, Object>> read = stringRedisTemplate.opsForStream().read(
                            Consumer.from(GROUP, NAME),
                            StreamReadOptions.empty().count(1).block(Duration.ofSeconds(2)),
                            StreamOffset.create(SECKILL_KEY, ReadOffset.lastConsumed())
                    );
                    if (ObjectUtils.isEmpty(read)) {
                        // 不存在数据，继续监听
                        if (log.isDebugEnabled()) {
                            log.debug("未找到符合条件的消息……");
                        }
                        continue;
                    }

                    // 解析数据
                    MapRecord<String, Object, Object> first = read.getFirst();
                    Map<Object, Object> value = first.getValue();
                    SeckillModel seckillModel = JSON.parseObject(JSON.toJSONString(value), SeckillModel.class);

                    // 执行下单逻辑(采用代理，生效事务)
                    streamSeckillService.createOrder(seckillModel);

                    // 确认消息xack
                    stringRedisTemplate.opsForStream().acknowledge(SECKILL_KEY, GROUP, first.getId());
                } catch (Exception e) {
                    log.error("系统错误", e);
                    try {
                        // 出现异常，休眠2秒
                        Thread.sleep(2000);
                    } catch (InterruptedException ignore) {}
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void createOrder(SeckillModel seckillModel) {
        // 校验该用户是否下单过(每个活动每个用户只能下一笔)
        String lockKey = String.format("activity:%s:order:user:%s", seckillModel.getActivityId(), seckillModel.getUserId());
        boolean lock = luaLockService.lock(lockKey, seckillModel.getUserId(), 30L * 24 * 60 * 60);
        if (!lock) {
            log.info("[活动id:{},用户id:{}]下单失败，活动用户已下单过", seckillModel.getActivityId(), seckillModel.getUserId());
            return;
        }
        try {
            // 创建订单 TODO
            log.info("[活动id:{},用户id:{}]创建订单成功", seckillModel.getActivityId(), seckillModel.getUserId());
        } catch (Exception e) {
            // 创建订单失败，释放锁
            luaLockService.unlock(lockKey);
            // 抛出异常，回滚事务
            throw e;
        }
    }

    public void order(SeckillModel seckillModel) {
        log.info("[activityId:{},userId:{}]开始发送下单消息……", seckillModel.getActivityId(), seckillModel.getUserId());
        // 判断group是否存在，不存在创建group
        if (StringUtils.isBlank(seckillModel.getOrderId())) {
            seckillModel.setOrderId(UUID.randomUUID().toString());
        }
        List<String> keyList = new ArrayList<>(Collections.singletonList(SECKILL_KEY));
        Long execute = stringRedisTemplate.execute(script, keyList, seckillModel.getOrderId(), seckillModel.getUserId(), seckillModel.getActivityId(), GROUP);
        if (null != execute && execute == 0) {
            log.info("[activityId:{},userId:{}]发送下单消息成功……", seckillModel.getActivityId(), seckillModel.getUserId());
        } else {
            log.info("[activityId:{},userId:{}]发送下单消息失败……", seckillModel.getActivityId(), seckillModel.getUserId());
        }
    }

}

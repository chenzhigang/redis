package org.czg.redis.center.biz.lock.seckill.watch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author czg
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WatchService {

    private final RedisTemplate<String, Object> redisTemplate;

    public List<String> seckillByWatch(String key) {
        List<String> resultList = new ArrayList<>();
        String info = "请求:%s:数量:%s:[%s]:剩余库存:%s";
        for (int i = 1; i <= 150; i++) {
            final int index = i;
            // 随机生成(1,10)的请求数量
            final long amount = 1 + (long) (Math.random() * 10);
            Long remainStock = null;
            try {
                // redis事务必须在SessionCallback中操作，不然不会生效
                remainStock = redisTemplate.execute(new SessionCallback<>() {
                    @SuppressWarnings("NullableProblems")
                    @Override
                    public <K, V> Long execute(RedisOperations<K, V> operations) throws DataAccessException {
                        Long resultStock = null;
                        while (true) {
                            @SuppressWarnings("unchecked") K k = (K) key;
                            // 监控库存
                            operations.watch(k);

                            // 判断库存是否存在
                            Object o = operations.opsForValue().get(k);
                            if (null == o) {
                                log.info(String.format(info, index, amount, "该商品缓存不存在", 0));
                                operations.unwatch();
                                break;
                            }

                            // 库存
                            long stock = Long.parseLong(o.toString());

                            // 判断库存是否充足
                            if (stock <= 0 || amount > stock) {
                                log.info(String.format(info, index, amount, "库存不足，剩余库存", stock));
                                operations.unwatch();
                                break;
                            }

                            // 开启事务
                            operations.multi();

                            // 执行扣库存操作，并且执行后续相关操作
                            operations.opsForValue().decrement(k, amount);

                            // 提交事务，如果在此期间库存被其他客户端修改，则exec返回null，即不为空表示秒杀成功
                            List<Object> exec = operations.exec();
                            if (ObjectUtils.isNotEmpty(exec)) {
                                log.info(String.format(info, index, amount, "秒杀成功", (stock - amount)));
                                resultStock = stock - amount;
                                break;
                            }
                        }
                        return resultStock;
                    }
                });
            } catch (Exception e) {
                log.error("系统异常", e);
            }
            if (null != remainStock) {
                resultList.add(String.format(info, index, amount, "秒杀成功", remainStock));
            } else {
                resultList.add(String.format(info, index, amount, "秒杀失败", 0));
            }
        }
        return resultList;
    }

}

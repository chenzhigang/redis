package org.czg.redis.center.biz.message.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.czg.redis.center.biz.message.service.DelayMessageService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author czg
 */
@EnableScheduling
@Component
@RequiredArgsConstructor
@Slf4j
public class DelayMessageTask {

    private final DelayMessageService delayMessageService;

    @Scheduled(fixedDelay = 1000)
    public void deal() {
        double min = 0;
        double max = System.currentTimeMillis();
        Set<Object> delayMessageSet = delayMessageService.popDelayMessage(min, max);
        // 开始消费
        if (ObjectUtils.isEmpty(delayMessageSet)) {
            if (log.isDebugEnabled()) {
                log.debug("监听中……");
            }
            return;
        }
        // 遍历消费
        for (Object o : delayMessageSet) {
            try {
                log.info("消费完成：{}", o.toString());
                // 移除队列
                delayMessageService.removeDelayMessage(o);
            } catch (Exception e) {
                log.error("消费失败：{}", o.toString());
            }
        }
    }

}

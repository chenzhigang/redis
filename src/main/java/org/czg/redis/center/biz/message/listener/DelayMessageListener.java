package org.czg.redis.center.biz.message.listener;

import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.message.contants.Constants;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * @author czg
 */
@Component
@Slf4j
public class DelayMessageListener extends KeyExpirationEventMessageListener {

    public DelayMessageListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("message:{}", message);
        String content = message.toString();
        // 延迟消息
        if (content.startsWith(Constants.ORDER_DELAY_PREFIX)) {
            Long orderId = Long.parseLong(content.split(Constants.ORDER_DELAY_PREFIX)[1].substring(1));
            log.info("订单id：{}", orderId);
        }
    }
}

package org.czg.redis.center.biz.message.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @author czg
 */
@Slf4j
@Component
public class MyScribeListener2 implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String receiveMessage = new String(message.getBody());
        log.info("接收到消息2：{}", receiveMessage);
    }
}

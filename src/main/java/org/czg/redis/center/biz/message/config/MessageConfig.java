package org.czg.redis.center.biz.message.config;

import org.czg.redis.center.biz.message.listener.MyScribeListener;
import org.czg.redis.center.biz.message.listener.MyScribeListener2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @author czg
 */
@Configuration
public class MessageConfig {

    @Bean
    public MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(new MyScribeListener());
    }

    @Bean
    public MessageListenerAdapter messageListener2() {
        return new MessageListenerAdapter(new MyScribeListener2());
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory factory) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(messageListener(), new ChannelTopic("channel1"));
        container.addMessageListener(messageListener2(), new ChannelTopic("channel2"));
        return container;
    }

}

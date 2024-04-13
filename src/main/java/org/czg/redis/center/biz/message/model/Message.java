package org.czg.redis.center.biz.message.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.groups.Default;
import lombok.Data;

/**
 * @author czg
 */
@Data
public class Message {

    @NotBlank(message = "通道不能为空", groups = {Default.class, SubscribeInterface.class})
    private String channel;

    @NotBlank(message = "消息内容不能为空")
    private String message;

    public interface SubscribeInterface {

    }

}

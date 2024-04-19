package org.czg.redis.center.biz.message.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author czg
 */
@Data
public class DelayMessage {

    @NotBlank(message = "消息内容不能为空")
    private String content;

}

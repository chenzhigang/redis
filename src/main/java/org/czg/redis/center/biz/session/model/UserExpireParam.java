package org.czg.redis.center.biz.session.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author czg
 */
@Data
public class UserExpireParam {

    /**
     * 会话id
     */
    private String sessionId;

    @NotNull(message = "过期时间不能为空")
    private Long ttl;

}

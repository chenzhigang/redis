package org.czg.redis.center.biz.bitmap.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author czg
 */
@Data
public class UserOnlineModel {

    @NotNull(message = "用户id")
    private Long userId;

}

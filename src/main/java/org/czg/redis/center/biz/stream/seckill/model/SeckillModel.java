package org.czg.redis.center.biz.stream.seckill.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author czg
 */
@Data
public class SeckillModel {

    @NotBlank(message = "活动id不能为空")
    private String activityId;

    @NotBlank(message = "用户id不能为空")
    private String userId;

    private String orderId;

}

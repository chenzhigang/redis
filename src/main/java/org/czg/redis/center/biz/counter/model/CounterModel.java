package org.czg.redis.center.biz.counter.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author czg
 */
@Data
public class CounterModel {

    @NotBlank(message = "事件不能为空")
    private String event;

    @NotBlank(message = "用户行为不能为空")
    private String behavior;

}

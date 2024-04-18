package org.czg.redis.center.biz.geo.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author czg
 */
@Data
public class NearbyModel {

    /**
     * 经度
     */
    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @NotNull(message = "纬度不能为空")
    private BigDecimal latitude;

    /**
     * 附近距离（单位：km）
     */
    @NotNull(message = "附近距离不能为空")
    private BigDecimal distance;

}

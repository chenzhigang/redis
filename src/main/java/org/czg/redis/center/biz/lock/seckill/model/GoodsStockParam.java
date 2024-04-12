package org.czg.redis.center.biz.lock.seckill.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author czg
 */
@Data
public class GoodsStockParam {

    @NotBlank(message = "商品信息不能为空")
    private String goodsInfo;

    @Min(value = 0, message = "商品库存不能为负数")
    @NotNull(message = "商品库存数量不能为空")
    private Long stock;

}

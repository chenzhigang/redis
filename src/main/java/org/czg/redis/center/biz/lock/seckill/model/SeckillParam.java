package org.czg.redis.center.biz.lock.seckill.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author czg
 */
@Data
public class SeckillParam {

    @NotBlank(message = "商品信息不能为空")
    private String goodsInfo;

    @Min(value = 1, message = "商品购买数量必须为正整数")
    @NotNull(message = "商品购买数量不能为空")
    private Long purchaseQuantity;

}

package org.czg.redis.center.biz.lock.stock.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author czg
 */
@Data
public class StockModel {

    @NotBlank(message = "库存键不能为空")
    private String key;

    @Min(value = 1, message = "库存扣减数量必须为正整数")
    @NotNull(message = "库存扣减数量不能为空")
    private Long quantity;

}

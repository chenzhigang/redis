package org.czg.redis.center.biz.bitmap.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Data;

/**
 * @author czg
 */
@Data
public class BitMap {

    /**
     * 键
     */
    @NotBlank(message = "键不能为空", groups = {Default.class, GetBitInterface.class})
    private String key;

    /**
     * 偏移量
     */
    @NotNull(message = "偏移量不能为空", groups = {Default.class, GetBitInterface.class})
    private Long offset;

    /**
     * 键值，1或0
     */
    @NotNull(message = "键值不能为空")
    private Boolean value;

    public interface GetBitInterface {

    }

}

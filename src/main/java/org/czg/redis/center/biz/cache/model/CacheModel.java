package org.czg.redis.center.biz.cache.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author czg
 */
@Data
public class CacheModel {

    @NotBlank(message = "redis键不能为空")
    private String key;

    @NotBlank(message = "redis键值不能为空", groups = {ValueSetInterface.class})
    private String value;

    /**
     * 过期时间（单位：毫秒）
     */
    private Long ttl;

    public interface ValueSetInterface {

    }

    public interface ValueGetInterface {

    }

}

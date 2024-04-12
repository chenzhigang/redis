package org.czg.redis.center.biz.lock.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Data;

/**
 * @author czg
 */
@Data
public class LockParam {

    @NotBlank(message = "锁键不能为空", groups = {Default.class, ReleaseLockInterface.class, ReleaseLockForValueInterface.class})
    private String key;

    @NotBlank(message = "锁值不能为空", groups = {Default.class, ReleaseLockForValueInterface.class})
    private String value;

    @NotNull(message = "锁过期时间不能为空")
    private Long ttl;

    /**
     * 重试次数
     */
    @Min(value = 1, message = "加锁重试次数必须为正整数")
    @NotNull(message = "加锁重试次数不能为空", groups = {RetryLockInterface.class})
    private Integer retryTimes;

    public interface ReleaseLockInterface {

    }

    public interface RetryLockInterface {

    }

    public interface ReleaseLockForValueInterface {

    }

}

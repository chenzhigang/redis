package org.czg.redis.center.biz.geo.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author czg
 */
@Data
public class PersonModel implements Serializable {

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    private String username;

    /**
     * 年龄
     */
    @Min(value = 1, message = "年龄不能小于0")
    @NotNull(message = "年龄不能为空")
    private Integer age;

    /**
     * 性别：0男，1女
     */
    @NotNull(message = "性别不能为空")
    private Integer gender;

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

}

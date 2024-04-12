package org.czg.redis.center.biz.rank.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author czg
 */
@Data
public class RankParam {

    @NotBlank(message = "用户姓名不能为空")
    private String username;

    @NotNull(message = "用户得分不能为空")
    private Integer score;

}

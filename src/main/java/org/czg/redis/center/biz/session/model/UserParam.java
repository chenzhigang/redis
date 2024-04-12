package org.czg.redis.center.biz.session.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author czg
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserParam {

    @NotBlank(message = "用户姓名不能为空")
    private String username;

    @NotBlank(message = "用户密码不能为空")
    private String password;

    /**
     * 过期时间
     */
    private Long ttl;

}

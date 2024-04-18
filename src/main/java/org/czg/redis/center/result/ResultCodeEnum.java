package org.czg.redis.center.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author czg
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ResultCodeEnum {
    SUCCESS("0000", "请求成功"),
    FAILED("9999", "请求失败"),
    SERVER_ERROR("5000", "系统错误"),
    PARAM_VALID_ERROR("1001", "参数校验错误"),
    USER_NOT_EXISTS("2001", "用户不存在")
    ;

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回码对应的描述信息
     */
    private String message;

}

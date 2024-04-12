package org.czg.redis.center.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.czg.redis.center.result.ResultCodeEnum;

/**
 * @author czg
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {

    /**
     * 异常返回码
     */
    private String code;

    /**
     * 异常返回描述信息
     */
    private String message;

    public BusinessException(String message) {
        this.code = ResultCodeEnum.SERVER_ERROR.getCode();
        this.message = message;
    }

    public BusinessException(String message, String code) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

    public BusinessException(Throwable cause, String message) {
        super(cause);
        this.message = message;
        this.code = ResultCodeEnum.SERVER_ERROR.getCode();
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }
}

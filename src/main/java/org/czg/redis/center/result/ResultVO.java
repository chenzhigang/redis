package org.czg.redis.center.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResultVO<T> {

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回描述
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    public ResultVO(T data) {
        this.data = data;
    }

    public ResultVO(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> ResultVO<T> success() {
        return new ResultVO<>(ResultCodeEnum.SUCCESS.getCode(), "请求成功");
    }

    public static <T> ResultVO<T> success(ResultCodeEnum resultCodeEnum) {
        return new ResultVO<>(resultCodeEnum.getCode(), resultCodeEnum.getMessage());
    }

    public static <T> ResultVO<T> success(String code, String message) {
        return new ResultVO<>(code, message);
    }

    public static <T> ResultVO<T> success(T data) {
        return new ResultVO<>(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage(), data);
    }

    public static <T> ResultVO<T> failed(ResultCodeEnum resultCodeEnum) {
        return new ResultVO<>(resultCodeEnum.getCode(), resultCodeEnum.getMessage());
    }

    public static <T> ResultVO<T> failed(String code, String message) {
        return new ResultVO<>(code, message);
    }

    public static <T> ResultVO<T> failed(T data) {
        return new ResultVO<>(ResultCodeEnum.FAILED.getCode(), ResultCodeEnum.FAILED.getMessage(), data);
    }

    public static <T> ResultVO<T> failed() {
        return new ResultVO<>(ResultCodeEnum.FAILED.getCode(), "请求失败");
    }
}

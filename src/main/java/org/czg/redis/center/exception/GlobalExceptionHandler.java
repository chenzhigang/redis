package org.czg.redis.center.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.result.ResultCodeEnum;
import org.czg.redis.center.result.ResultVO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;
import java.util.Set;

/**
 * @author czg
 */
@Slf4j
@ControllerAdvice
@Component
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResultVO<Object> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        assert fieldError != null;
        log.warn("参数校验异常:{}({})", fieldError.getDefaultMessage(), fieldError.getField());
        return ResultVO.failed(ResultCodeEnum.PARAM_VALID_ERROR.getCode(), fieldError.getDefaultMessage());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public ResultVO<Object> constraintViolationException(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> eSet = exception.getConstraintViolations();
        String resultMsg = null;
        if (!CollectionUtils.isEmpty(eSet)) {
            Iterator<ConstraintViolation<?>> iterator = eSet.iterator();
            if (iterator.hasNext()) {
                log.warn("参数校验异常:{}", iterator.next().getMessage());
                resultMsg = iterator.next().getMessage();
            }
        }
        return ResultVO.failed(ResultCodeEnum.PARAM_VALID_ERROR.getCode(), resultMsg);
    }

    @ExceptionHandler(value = MissingRequestHeaderException.class)
    @ResponseBody
    public ResultVO<Object> missingRequestHeaderException(MissingRequestHeaderException exception) {
        String msg = String.format("缺少请求头参数：%s", exception.getHeaderName());
        return ResultVO.failed(ResultCodeEnum.PARAM_VALID_ERROR.getCode(), msg);
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ResultVO<Object> businessException(BusinessException exception) {
        log.error("业务异常", exception);
        return ResultVO.failed(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultVO<Object> otherException(Exception exception) {
        log.error("系统异常", exception);
        return ResultVO.failed(ResultCodeEnum.PARAM_VALID_ERROR.getCode(), exception.getMessage());
    }

}

package org.czg.redis.center.biz.lock.lua.controller;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.lock.lua.service.LuaLockService;
import org.czg.redis.center.biz.lock.model.LockParam;
import org.czg.redis.center.result.ResultVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author czg
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/lock/lua")
public class LuaLockController {

    private final LuaLockService luaLockService;

    /**
     * 加锁
     *
     * @param lockParam 加锁参数
     * @return 返回结果
     */
    @PostMapping("/acquire")
    public ResultVO<Void> acquireLock(@Valid @RequestBody LockParam lockParam) {
        luaLockService.lock(lockParam.getKey(), lockParam.getValue(), lockParam.getTtl());
        return ResultVO.success();
    }

    /**
     * 加锁（重试）
     *
     * @param lockParam 加锁参数
     * @return 返回结果
     */
    @PostMapping("/acquireForRetry")
    public ResultVO<Void> acquireForRetryLock(@Validated(value = {Default.class, LockParam.RetryLockInterface.class}) @RequestBody LockParam lockParam) {
        luaLockService.lockForRetry(lockParam.getKey(), lockParam.getValue(), lockParam.getTtl(), lockParam.getRetryTimes());
        return ResultVO.success();
    }

    /**
     * 解锁
     *
     * @param lockParam 解锁参数
     * @return 返回结果
     */
    @PostMapping("/release")
    public ResultVO<Void> releaseLock(@Validated(value = {LockParam.ReleaseLockInterface.class}) @RequestBody LockParam lockParam) {
        luaLockService.unlock(lockParam.getKey());
        return ResultVO.success();
    }

    /**
     * 解锁（匹配键值）
     *
     * @param lockParam 加锁参数
     * @return 返回结果
     */
    @PostMapping("/releaseForValue")
    public ResultVO<Void> releaseForValueLock(@Validated(value = {LockParam.ReleaseLockForValueInterface.class}) @RequestBody LockParam lockParam) {
        luaLockService.unlockForValue(lockParam.getKey(), lockParam.getValue());
        return ResultVO.success();
    }

}

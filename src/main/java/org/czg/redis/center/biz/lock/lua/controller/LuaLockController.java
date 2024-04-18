package org.czg.redis.center.biz.lock.lua.controller;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.lock.lua.service.LuaLockService;
import org.czg.redis.center.biz.lock.model.LockModel;
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
     * @param lockModel 加锁参数
     * @return 返回结果
     */
    @PostMapping("/acquire")
    public ResultVO<Void> acquireLock(@Valid @RequestBody LockModel lockModel) {
        luaLockService.lock(lockModel.getKey(), lockModel.getValue(), lockModel.getTtl());
        return ResultVO.success();
    }

    /**
     * 加锁（重试）
     *
     * @param lockModel 加锁参数
     * @return 返回结果
     */
    @PostMapping("/acquireForRetry")
    public ResultVO<Void> acquireForRetryLock(@Validated(value = {Default.class, LockModel.RetryLockInterface.class}) @RequestBody LockModel lockModel) {
        luaLockService.lockForRetry(lockModel.getKey(), lockModel.getValue(), lockModel.getTtl(), lockModel.getRetryTimes());
        return ResultVO.success();
    }

    /**
     * 解锁
     *
     * @param lockModel 解锁参数
     * @return 返回结果
     */
    @PostMapping("/release")
    public ResultVO<Void> releaseLock(@Validated(value = {LockModel.ReleaseLockInterface.class}) @RequestBody LockModel lockModel) {
        luaLockService.unlock(lockModel.getKey());
        return ResultVO.success();
    }

    /**
     * 解锁（匹配键值）
     *
     * @param lockModel 加锁参数
     * @return 返回结果
     */
    @PostMapping("/releaseForValue")
    public ResultVO<Void> releaseForValueLock(@Validated(value = {LockModel.ReleaseLockForValueInterface.class}) @RequestBody LockModel lockModel) {
        luaLockService.unlockForValue(lockModel.getKey(), lockModel.getValue());
        return ResultVO.success();
    }

}

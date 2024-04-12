package org.czg.redis.center.biz.lock.ordinary.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.lock.model.LockParam;
import org.czg.redis.center.biz.lock.ordinary.service.OrdinaryLockService;
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
@RequestMapping("/lock/ordinary")
public class OrdinaryLockController {

    private final OrdinaryLockService ordinaryLockService;

    @PostMapping("/acquire")
    public ResultVO<Void> acquireLock(@Valid @RequestBody LockParam lockParam) {
        ordinaryLockService.lock(lockParam.getKey(), lockParam.getValue(), lockParam.getTtl());
        return ResultVO.success();
    }

    @PostMapping("/release")
    public ResultVO<Void> releaseLock(@Validated(value = {LockParam.ReleaseLockInterface.class}) @RequestBody LockParam lockParam) {
        ordinaryLockService.unlock(lockParam.getKey());
        return ResultVO.success();
    }

}

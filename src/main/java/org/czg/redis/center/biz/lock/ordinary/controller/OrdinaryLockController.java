package org.czg.redis.center.biz.lock.ordinary.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.lock.model.LockModel;
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
    public ResultVO<Void> acquireLock(@Valid @RequestBody LockModel lockModel) {
        ordinaryLockService.lock(lockModel.getKey(), lockModel.getValue(), lockModel.getTtl());
        return ResultVO.success();
    }

    @PostMapping("/release")
    public ResultVO<Void> releaseLock(@Validated(value = {LockModel.ReleaseLockInterface.class}) @RequestBody LockModel lockModel) {
        ordinaryLockService.unlock(lockModel.getKey());
        return ResultVO.success();
    }

}

package org.czg.redis.center.biz.counter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.counter.model.CounterParam;
import org.czg.redis.center.biz.counter.service.CounterService;
import org.czg.redis.center.result.ResultVO;
import org.springframework.web.bind.annotation.*;

/**
 * @author czg
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/counter")
public class CounterController {

    private final CounterService counterService;

    @PostMapping("/incr")
    public ResultVO<Void> incrBehavior(@Valid @RequestBody CounterParam counterParam) {
        counterService.incrBehavior(counterParam.getEvent(), counterParam.getBehavior());
        return ResultVO.success();
    }

    @PostMapping("/decr")
    public ResultVO<Void> decrBehavior(@Valid @RequestBody CounterParam counterParam) {
        counterService.decrBehavior(counterParam.getEvent(), counterParam.getBehavior());
        return ResultVO.success();
    }

    @GetMapping("/info")
    public ResultVO<Long> getBehaviorCounter(@Valid @RequestBody CounterParam counterParam) {
        return ResultVO.success(counterService.getBehaviorCounter(counterParam.getEvent(), counterParam.getBehavior()));
    }

}

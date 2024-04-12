package org.czg.redis.center.biz.cache.controller;

import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.czg.redis.center.biz.cache.model.CacheParam;
import org.czg.redis.center.biz.cache.service.DemoService;
import org.czg.redis.center.result.ResultVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RequestMapping("/cache")
@RestController
public class DemoContrller {

    private final DemoService demoService;

    @PostMapping("/value/set")
    public ResultVO<Boolean> setValue(@Validated(value = {Default.class, CacheParam.ValueSetInterface.class}) @RequestBody CacheParam cacheParam) {
        demoService.setValue(cacheParam.getKey(), cacheParam.getValue(), cacheParam.getTtl());
        return ResultVO.success();
    }

    @GetMapping("/value/get")
    public ResultVO<String> getValue(@Validated(value = {Default.class, CacheParam.ValueGetInterface.class}) @RequestBody CacheParam cacheParam) {
        return ResultVO.success(demoService.getValue(cacheParam.getKey()));
    }

}

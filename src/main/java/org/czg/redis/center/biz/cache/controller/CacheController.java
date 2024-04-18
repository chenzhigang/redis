package org.czg.redis.center.biz.cache.controller;

import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.czg.redis.center.biz.cache.model.CacheModel;
import org.czg.redis.center.biz.cache.service.CacheService;
import org.czg.redis.center.result.ResultVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author czg
 */
@Validated
@RequiredArgsConstructor
@RequestMapping("/cache")
@RestController
public class CacheController {

    private final CacheService cacheService;

    @PostMapping("/value/set")
    public ResultVO<Boolean> setValue(@Validated(value = {Default.class, CacheModel.ValueSetInterface.class}) @RequestBody CacheModel cacheModel) {
        cacheService.setValue(cacheModel.getKey(), cacheModel.getValue(), cacheModel.getTtl());
        return ResultVO.success();
    }

    @GetMapping("/value/get")
    public ResultVO<String> getValue(@Validated(value = {Default.class, CacheModel.ValueGetInterface.class}) @RequestBody CacheModel cacheModel) {
        return ResultVO.success(cacheService.getValue(cacheModel.getKey()));
    }

}

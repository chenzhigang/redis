package org.czg.redis.center.biz.lock.seckill.watch.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.lock.seckill.model.SeckillParam;
import org.czg.redis.center.biz.lock.seckill.watch.service.WatchService;
import org.czg.redis.center.result.ResultVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author czg
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/lock/watch")
public class WatchController {

    private final WatchService watchService;

    /**
     * 秒杀
     *
     * @param seckillParam 请求参数
     * @return 返回结果
     */
    @PostMapping("/seckill")
    public ResultVO<List<String>> seckill(@Valid @RequestBody SeckillParam seckillParam) {
        return ResultVO.success(watchService.seckillByWatch(seckillParam.getGoodsInfo()));
    }

}

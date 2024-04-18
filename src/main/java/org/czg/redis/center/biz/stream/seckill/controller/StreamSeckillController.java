package org.czg.redis.center.biz.stream.seckill.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.stream.seckill.model.SeckillModel;
import org.czg.redis.center.biz.stream.seckill.service.StreamSeckillService;
import org.czg.redis.center.result.ResultVO;
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
@RequestMapping("/stream")
public class StreamSeckillController {

    private final StreamSeckillService streamSeckillService;

    /**
     * 秒杀
     *
     * @param seckillModel 下单数据
     * @return 返回结果
     */
    @PostMapping("/seckillOrder")
    public ResultVO<Void> seckillOrder(@Valid @RequestBody SeckillModel seckillModel) {
        streamSeckillService.order(seckillModel);
        return ResultVO.success();
    }

}

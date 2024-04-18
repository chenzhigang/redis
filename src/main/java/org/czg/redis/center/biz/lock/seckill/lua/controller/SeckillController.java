package org.czg.redis.center.biz.lock.seckill.lua.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.lock.seckill.model.GoodsStockModel;
import org.czg.redis.center.biz.lock.seckill.model.SeckillModel;
import org.czg.redis.center.biz.lock.seckill.lua.service.SeckillService;
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
@RequestMapping("/lock/seckill")
public class SeckillController {

    private final SeckillService seckillService;

    /**
     * 秒杀
     *
     * @param seckillModel 请求参数
     * @return 返回结果
     */
    @PostMapping("/order")
    public ResultVO<String> seckill(@Valid @RequestBody SeckillModel seckillModel) {
        return ResultVO.success(seckillService.seckill(seckillModel.getGoodsInfo(), seckillModel.getPurchaseQuantity(), 1));
    }

    /**
     * 模拟多线程秒杀场景
     *
     * @param seckillModel 请求商品信息
     * @return 返回结果
     */
    @PostMapping("/order/fast")
    public ResultVO<List<String>> seckillFast(@Valid @RequestBody SeckillModel seckillModel) {
        return ResultVO.success(seckillService.seckillFast(seckillModel.getGoodsInfo()));
    }

    /**
     * 商品库存初始化
     *
     * @param goodsStockModel 请求参数
     * @return 返回参数
     */
    @PostMapping("/stock/init")
    public ResultVO<Void> initStock(@Valid @RequestBody GoodsStockModel goodsStockModel) {
        seckillService.initStock(goodsStockModel);
        return ResultVO.success();
    }
}

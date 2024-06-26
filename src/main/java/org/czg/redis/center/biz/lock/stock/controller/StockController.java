package org.czg.redis.center.biz.lock.stock.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.lock.stock.model.StockModel;
import org.czg.redis.center.biz.lock.stock.service.StockService;
import org.czg.redis.center.result.ResultVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author czg
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/lock/stock")
public class StockController {

    private final StockService stockService;

    /**
     * 库存扣减
     *
     * @param stockModel 请求参数
     * @return 返回结果
     */
    @PostMapping("/decr")
    public ResultVO<Void> decrStock(@Valid @RequestBody StockModel stockModel) {
        stockService.decrementStock(stockModel.getKey(), stockModel.getQuantity());
        return ResultVO.success();
    }

}

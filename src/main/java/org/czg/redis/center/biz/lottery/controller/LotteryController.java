package org.czg.redis.center.biz.lottery.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.lottery.service.LotteryService;
import org.czg.redis.center.result.ResultVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author czg
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/lottery")
public class LotteryController {

    private final LotteryService lotteryService;

    /**
     * 添加参与抽奖的人员
     *
     * @param participant 参与活动的人员信息
     * @return 返回结果
     */
    @PostMapping("/join")
    public ResultVO<Void> joinLottery(String participant) {
        lotteryService.joinLottery(participant);
        return ResultVO.success();
    }

    /**
     * 抽取一名中奖者
     *
     * @return 中奖者信息
     */
    @GetMapping("/drawWinner")
    public ResultVO<String> drawWinner() {
        return ResultVO.success(lotteryService.drawWinner());
    }

    /**
     * 抽取多名中奖者
     *
     * @param participantCount 中奖数量
     * @return 中奖者信息
     */
    @GetMapping("/drawWinners")
    public ResultVO<List<String>> drawWinners(Integer participantCount) {
        return ResultVO.success(lotteryService.drawWinner(participantCount));
    }

}

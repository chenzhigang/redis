package org.czg.redis.center.biz.rank.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.rank.model.RankModel;
import org.czg.redis.center.biz.rank.service.RankService;
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
@RequestMapping("/rank")
public class RankController {

    private final RankService rankService;

    @PostMapping("/add")
    public ResultVO<Void> addRank(@Valid @RequestBody RankModel rankModel) {
        rankService.addToRanking(rankModel.getUsername(), rankModel.getScore());
        return ResultVO.success();
    }

    @PostMapping("/get")
    public ResultVO<List<RankModel>> getRanking() {
        return ResultVO.success(rankService.getRanking());
    }

}

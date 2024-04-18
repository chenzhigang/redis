package org.czg.redis.center.biz.list.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.list.service.LatestListService;
import org.czg.redis.center.biz.list.model.LatestListModel;
import org.czg.redis.center.result.ResultVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author czg
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/list/latest")
public class LatestListController {

    private final LatestListService latestListService;

    @PostMapping("/push")
    public ResultVO<Void> push(@Valid @RequestBody LatestListModel latestListModel) {
        latestListService.push(latestListModel.getContent());
        return ResultVO.success();
    }

    @GetMapping("/getList")
    public ResultVO<List<Object>> getLatestList(@NotNull(message = "最新列表数据数量不能为空") @RequestParam(value = "count") Integer count) {
        return ResultVO.success(latestListService.getLatestList(count));
    }

}

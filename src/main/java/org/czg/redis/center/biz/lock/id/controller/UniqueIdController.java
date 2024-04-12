package org.czg.redis.center.biz.lock.id.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.lock.id.service.UniqueIdService;
import org.czg.redis.center.result.ResultVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author czg
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/lock/id")
public class UniqueIdController {

    private final UniqueIdService uniqueIdService;

    @PostMapping("/generator")
    public ResultVO<Long> generatorUniqueId(String key) {
        return ResultVO.success(uniqueIdService.getUniqueId(key));
    }

}

package org.czg.redis.center.biz.ratelimiter.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.ratelimiter.service.RateLimiterService;
import org.czg.redis.center.result.ResultVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author czg
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/rateLimiter")
public class RateLimierController {

    private final RateLimiterService rateLimiterService;

    @PostMapping("/req")
    public ResultVO<Void> rateRequest(HttpServletRequest request) {
        rateLimiterService.rateLimiter(request.getRequestURI());
        return ResultVO.success();
    }

}

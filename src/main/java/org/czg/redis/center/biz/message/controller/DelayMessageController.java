package org.czg.redis.center.biz.message.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.message.model.DelayMessage;
import org.czg.redis.center.biz.message.service.DelayMessageService;
import org.czg.redis.center.result.ResultVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * redis延迟消息
 * 1）监听过期key
 * 2）zset排序+定时任务监听
 *
 * @author czg
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/message/delay")
public class DelayMessageController {

    private final DelayMessageService delayMessageService;

    @PostMapping("/add")
    public ResultVO<Void> addDelayMessage() {
        delayMessageService.addMessage();
        return ResultVO.success();
    }

    @PostMapping("/push")
    public ResultVO<Void> pushDelayMessage(@Valid @RequestBody DelayMessage delayMessage) {
        delayMessageService.pushDelayMessage(delayMessage.getContent());
        return ResultVO.success();
    }

}

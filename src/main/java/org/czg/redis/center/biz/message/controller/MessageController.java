package org.czg.redis.center.biz.message.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.message.model.Message;
import org.czg.redis.center.biz.message.service.MessageService;
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
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/publish")
    public ResultVO<Void> publishAndSubscribe(@Valid @RequestBody Message message) {
        messageService.publish(message.getChannel(), message.getMessage());
        return ResultVO.success();
    }

}

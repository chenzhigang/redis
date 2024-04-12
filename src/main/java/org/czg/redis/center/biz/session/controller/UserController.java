package org.czg.redis.center.biz.session.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.czg.redis.center.biz.session.model.User;
import org.czg.redis.center.biz.session.model.UserExpireParam;
import org.czg.redis.center.biz.session.model.UserParam;
import org.czg.redis.center.biz.session.service.UserService;
import org.czg.redis.center.result.ResultVO;
import org.springframework.web.bind.annotation.*;

/**
 * @author czg
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/session/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResultVO<String> login(@Valid @RequestBody UserParam userParam, HttpSession session) {
        return ResultVO.success(userService.login(userParam, session));
    }

    @GetMapping("/info")
    public ResultVO<User> getUserInfo(@NotBlank(message = "sessionId不能为空") @RequestHeader String sessionId, HttpSession session) {
        return ResultVO.success(userService.getUserInfo(sessionId, session));
    }

    @PostMapping("/expire")
    public ResultVO<Void> setUserExpire(@Valid @RequestBody UserExpireParam userExpireParam, HttpSession session) {
        userService.setUserExpire(userExpireParam, session);
        return ResultVO.success();
    }

}

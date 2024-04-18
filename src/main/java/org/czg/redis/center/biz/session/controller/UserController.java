package org.czg.redis.center.biz.session.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.czg.redis.center.biz.session.model.User;
import org.czg.redis.center.biz.session.model.UserExpireModel;
import org.czg.redis.center.biz.session.model.UserModel;
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
    public ResultVO<String> login(@Valid @RequestBody UserModel userModel, HttpSession session) {
        return ResultVO.success(userService.login(userModel, session));
    }

    @GetMapping("/info")
    public ResultVO<User> getUserInfo(@NotBlank(message = "sessionId不能为空") @RequestHeader String sessionId, HttpSession session) {
        return ResultVO.success(userService.getUserInfo(sessionId, session));
    }

    @PostMapping("/expire")
    public ResultVO<Void> setUserExpire(@Valid @RequestBody UserExpireModel userExpireModel, HttpSession session) {
        userService.setUserExpire(userExpireModel, session);
        return ResultVO.success();
    }

}

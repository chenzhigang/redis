package org.czg.redis.center.biz.session.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.czg.redis.center.biz.session.model.User;
import org.czg.redis.center.biz.session.model.UserExpireModel;
import org.czg.redis.center.biz.session.model.UserModel;
import org.czg.redis.center.biz.session.util.RedisSessionUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author czg
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final RedisSessionUtil redisSessionUtil;

    public User getUser(String username, String password) {
        return User.builder()
                .username(username)
                .password(password)
                .mobile("18042423259")
                .userId(1L)
                .build();
    }

    public String login(UserModel userModel, HttpSession session) {
        User user = getUser(userModel.getUsername(), userModel.getPassword());
        String sessionId = UUID.randomUUID().toString();
        session.setAttribute(sessionId, user);
        return sessionId;
    }

    public User getUserInfo(String sessionId, HttpSession session) {
        return (User) session.getAttribute(sessionId);
    }

    public void setUserExpire(UserExpireModel userExpireModel, HttpSession session) {
        String sessionId = StringUtils.isEmpty(userExpireModel.getSessionId()) ? session.getId() : userExpireModel.getSessionId();
        redisSessionUtil.setSessionExpire(sessionId, userExpireModel.getTtl());
    }
}

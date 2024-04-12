package org.czg.redis.center.biz.counter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author czg
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum BehaviorEnum {
    LIKE("LIKE", "点赞"),
    UNLIKE("UNLIKE", "取消点赞"),
    FAVORITE("FAVORITE",  "收藏"),
    CANCEL_FAVORITE("CANCEL_FAVORITE", "取消收藏"),
    FOLLOW("FOLLOW", "关注"),
    UNFOLLOW("UNFOLLOW", "取消关注"),
    BROWSE("BROWSE", "浏览"),
    FORWARD("FORWARD", "转发"),
    TOP("TOP", "置顶"),
    CANCEL_TOP("CANCEL_TOP", "取消置顶"),
    SEARCH("SEARCH", "搜索"),
    REGISTER("REGISTER", "注册"),
    LOGIN("LOGIN", "登录"),
    LOGOUT("LOGOUT", "退出登录"),
    ;

    /**
     * 用户行为编码值
     */
    private String behavior;

    /**
     * 行为描述
     */
    private String desc;

    public static BehaviorEnum of(String behavior) {
        if (StringUtils.isBlank(behavior)) {
            return null;
        }
        for (BehaviorEnum behaviorEnum : BehaviorEnum.values()) {
            if (behaviorEnum.getBehavior().equals(behavior)) {
                return behaviorEnum;
            }
        }
        return null;
    }
}

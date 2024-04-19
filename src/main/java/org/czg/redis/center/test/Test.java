package org.czg.redis.center.test;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author czg
 */
@Slf4j
public class Test {

    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.of(2024, 4, 18, 17, 52);
        while (true) {
            log.info("监听中……");
            long currTime = System.currentTimeMillis();
            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
            Instant instant = zonedDateTime.toInstant();
            if (instant.toEpochMilli() < currTime) {
                log.info("开始执行延迟消息");
                break;
            }
        }
    }

}

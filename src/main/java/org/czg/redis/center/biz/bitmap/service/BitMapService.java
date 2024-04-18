package org.czg.redis.center.biz.bitmap.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.czg.redis.center.biz.bitmap.model.BitMap;
import org.czg.redis.center.biz.bitmap.model.UserSign;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author czg
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BitMapService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final StringRedisTemplate stringRedisTemplate;

    private final static String DATE_FORMAT_YM = "yyyyMM";

    private final static String DATE_FORMAT_YMD = "yyyyMMdd";

    private final static String ONLINE_STR = "user:online:%s";

    private final static String SIGN_STR = "user:sign:%s:%s";

    public void setBit(BitMap bit) {
        redisTemplate.opsForValue().setBit(bit.getKey(), bit.getOffset(), bit.getValue());
    }

    public void setBit(String key, Long offset, Boolean value) {
        redisTemplate.opsForValue().setBit(key, offset, value);
    }

    public Boolean getBit(BitMap bit) {
        return redisTemplate.opsForValue().getBit(bit.getKey(), bit.getOffset());
    }

    public Integer signContinueCount(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        String dateTimeStr = now.format(DateTimeFormatter.ofPattern(DATE_FORMAT_YM));
        List<Long> fieldList = getFieldList(userId);
        if (ObjectUtils.isEmpty(fieldList)) {
            log.info("用户[{}]，{}未签到过", userId, dateTimeStr);
            return 0;
        }
        Long fieldValue = fieldList.getFirst();
        if (null == fieldValue || fieldValue == 0) {
            log.info("用户[{}]，{}未签到过", userId, dateTimeStr);
            return 0;
        }
        AtomicInteger resultCount = new AtomicInteger(0);
        while ((fieldValue & 1) != 0) {
            resultCount.getAndIncrement();
            // 右移一位，即将二进制最后一位移除，并且在最前面位补0
            fieldValue >>>= 1;
        }
        return resultCount.get();
    }

    public void sign(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        int dayOfMonth = now.getDayOfMonth();
        String dateTimeStr = now.format(DateTimeFormatter.ofPattern(DATE_FORMAT_YM));
        setBit(String.format(SIGN_STR, userId, dateTimeStr), (long) (dayOfMonth - 1), true);
    }

    public void signMakeUp(UserSign userSign) {
        setBit(String.format(SIGN_STR, userSign.getUserId(), userSign.getDateTimeStr()),
                (long) userSign.getDayOfMonth() - 1, true);
    }

    public Integer maxSignContinueCount(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        String dateTimeStr = now.format(DateTimeFormatter.ofPattern(DATE_FORMAT_YM));
        List<Long> fieldList = getFieldList(userId);
        List<Integer> countList = new ArrayList<>();
        if (ObjectUtils.isEmpty(fieldList)) {
            log.info("用户[{}]，{}未签到过", userId, dateTimeStr);
            return 0;
        }
        Long fieldValue = fieldList.getFirst();
        if (null == fieldValue || fieldValue == 0) {
            log.info("用户[{}]，{}未签到过", userId, dateTimeStr);
            return 0;
        }
        int count = 0;
        while(true) {
            // 4294967295L = 2^32 - 1 = 二进制32个1
            // 判断两个数字&，是否存在1，即值不等于0
            if ((fieldValue & 4294967295L) == 0) {
                if (count != 0) {
                    countList.add(count);
                }
                break;
            }
            // 如果&值为1，则累加连续数量加1
            if ((fieldValue & 1) == 1) {
                count++;
            } else {
                // 如果&值为0，并且之前累计的连续数量不为0，则添加该连续数量，并且重置累计值
                if (count != 0) {
                    countList.add(count);
                }
                count = 0;
            }
            fieldValue >>>= 1;
        }
        log.info("countList:{}", countList);
        // 排序取最大的连续数量，即升序最后一个
        countList = countList.stream().sorted().collect(Collectors.toList());
        return ObjectUtils.isNotEmpty(countList) ? countList.getLast() : 0;
    }

    private List<Long> getFieldList(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        String dateTimeStr = now.format(DateTimeFormatter.ofPattern(DATE_FORMAT_YM));
        return redisTemplate.opsForValue()
                .bitField(String.format(SIGN_STR, userId, dateTimeStr),
                        BitFieldSubCommands.create()
                                .get(BitFieldSubCommands.BitFieldType.unsigned(32)).valueAt(0));
    }

    public void setUserOnline(Long userId) {
        String dayFormat = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_YMD));
        redisTemplate.opsForValue().setBit(String.format(ONLINE_STR, dayFormat), userId, true);
    }

    public void setUserUnOnline(Long userId) {
        String dayFormat = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_YMD));
        redisTemplate.opsForValue().setBit(String.format(ONLINE_STR, dayFormat), userId, false);
    }

    public Long getUserOnlineCount() {
        String dayFormat = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_YMD));
        return stringRedisTemplate.execute((RedisCallback<Long>) connection -> {
            StringRedisConnection redisConnection = (StringRedisConnection) connection;
            return redisConnection.bitCount(String.format(ONLINE_STR, dayFormat));
        });
    }
}

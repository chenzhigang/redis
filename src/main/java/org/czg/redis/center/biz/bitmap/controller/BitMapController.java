package org.czg.redis.center.biz.bitmap.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.bitmap.model.BitMap;
import org.czg.redis.center.biz.bitmap.model.UserOnline;
import org.czg.redis.center.biz.bitmap.model.UserSign;
import org.czg.redis.center.biz.bitmap.service.BitMapService;
import org.czg.redis.center.result.ResultVO;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/bitMap")
public class BitMapController {

    private final BitMapService bitMapService;

    @PostMapping("/set")
    public ResultVO<Void> setBitMap(@Valid @RequestBody BitMap bitMapParam) {
        bitMapService.setBit(bitMapParam);
        return ResultVO.success();
    }

    @PostMapping("/get")
    public ResultVO<Void> getBitMap(@Validated(value = {BitMap.GetBitInterface.class}) @RequestBody BitMap bitMapParam) {
        bitMapService.setBit(bitMapParam);
        return ResultVO.success();
    }

    /**
     * 签到
     *
     * @param userSign 请求参数
     * @return 返回结果
     */
    @PostMapping("/user/sign")
    public ResultVO<Void> sign(@Valid @RequestBody UserSign userSign) {
        bitMapService.sign(userSign.getUserId());
        return ResultVO.success();
    }

    /**
     * 补签
     *
     * @param userSign 请求参数
     * @return 返回结果
     */
    @PostMapping("/user/makeUpSign")
    public ResultVO<Void> makeUpSign(@Validated(value = {UserSign.MakeUpSignInterface.class}) @RequestBody UserSign userSign) {
        bitMapService.signMakeUp(userSign);
        return ResultVO.success();
    }

    /**
     * 统计当前月当前连续签到次数
     *
     * @param userSign 请求参数
     * @return 当前连续签到天数
     */
    @PostMapping("/user/signContinueCount")
    public ResultVO<Integer> signContinueCount(@Valid @RequestBody UserSign userSign) {
        return ResultVO.success(bitMapService.signContinueCount(userSign.getUserId()));
    }

    /**
     * 统计当月用户最大连续签到次数
     *
     * @param userSign 请求参数
     * @return 最大连续签到次数
     */
    @PostMapping("/user/maxSignContinueCount")
    public ResultVO<Integer> maxSignContinueCount(@Valid @RequestBody UserSign userSign) {
        return ResultVO.success(bitMapService.maxSignContinueCount(userSign.getUserId()));
    }

    /**
     * 用户设置在线
     *
     * @param userOnline 请求参数
     * @return 返回结果
     */
    @PostMapping("/user/online")
    public ResultVO<Void> setUserOnline(@Valid @RequestBody UserOnline userOnline) {
        bitMapService.setUserOnline(userOnline.getUserId());
        return ResultVO.success();
    }

    /**
     * 设置用户离线
     *
     * @param userOnline 请求参数
     * @return 返回结果
     */
    @PostMapping("/user/unOnline")
    public ResultVO<Void> setUserUnOnline(@Valid @RequestBody UserOnline userOnline) {
        bitMapService.setUserUnOnline(userOnline.getUserId());
        return ResultVO.success();
    }

    /**
     * 统计用户在线人数
     *
     * @return 用户在线人数
     */
    @PostMapping("/user/onlineCount")
    public ResultVO<Long> getUserOnlineCount() {
        return ResultVO.success(bitMapService.getUserOnlineCount());
    }

}

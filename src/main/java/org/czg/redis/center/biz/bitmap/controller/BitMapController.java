package org.czg.redis.center.biz.bitmap.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.bitmap.model.BitMapModel;
import org.czg.redis.center.biz.bitmap.model.UserOnlineModel;
import org.czg.redis.center.biz.bitmap.model.UserSignModel;
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
    public ResultVO<Void> setBitMap(@Valid @RequestBody BitMapModel bitMapModel) {
        bitMapService.setBit(bitMapModel);
        return ResultVO.success();
    }

    @PostMapping("/get")
    public ResultVO<Void> getBitMap(@Validated(value = {BitMapModel.GetBitInterface.class}) @RequestBody BitMapModel bitMapModel) {
        bitMapService.setBit(bitMapModel);
        return ResultVO.success();
    }

    /**
     * 签到
     *
     * @param userSignModel 请求参数
     * @return 返回结果
     */
    @PostMapping("/user/sign")
    public ResultVO<Void> sign(@Valid @RequestBody UserSignModel userSignModel) {
        bitMapService.sign(userSignModel.getUserId());
        return ResultVO.success();
    }

    /**
     * 补签
     *
     * @param userSignModel 请求参数
     * @return 返回结果
     */
    @PostMapping("/user/makeUpSign")
    public ResultVO<Void> makeUpSign(@Validated(value = {UserSignModel.MakeUpSignInterface.class}) @RequestBody UserSignModel userSignModel) {
        bitMapService.signMakeUp(userSignModel);
        return ResultVO.success();
    }

    /**
     * 统计当前月当前连续签到次数
     *
     * @param userSignModel 请求参数
     * @return 当前连续签到天数
     */
    @PostMapping("/user/signContinueCount")
    public ResultVO<Integer> signContinueCount(@Valid @RequestBody UserSignModel userSignModel) {
        return ResultVO.success(bitMapService.signContinueCount(userSignModel.getUserId()));
    }

    /**
     * 统计当月用户最大连续签到次数
     *
     * @param userSignModel 请求参数
     * @return 最大连续签到次数
     */
    @PostMapping("/user/maxSignContinueCount")
    public ResultVO<Integer> maxSignContinueCount(@Valid @RequestBody UserSignModel userSignModel) {
        return ResultVO.success(bitMapService.maxSignContinueCount(userSignModel.getUserId()));
    }

    /**
     * 用户设置在线
     *
     * @param userOnlineModel 请求参数
     * @return 返回结果
     */
    @PostMapping("/user/online")
    public ResultVO<Void> setUserOnline(@Valid @RequestBody UserOnlineModel userOnlineModel) {
        bitMapService.setUserOnline(userOnlineModel.getUserId());
        return ResultVO.success();
    }

    /**
     * 设置用户离线
     *
     * @param userOnlineModel 请求参数
     * @return 返回结果
     */
    @PostMapping("/user/unOnline")
    public ResultVO<Void> setUserUnOnline(@Valid @RequestBody UserOnlineModel userOnlineModel) {
        bitMapService.setUserUnOnline(userOnlineModel.getUserId());
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

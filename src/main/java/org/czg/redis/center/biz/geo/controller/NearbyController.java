package org.czg.redis.center.biz.geo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.geo.model.NearbyModel;
import org.czg.redis.center.biz.geo.model.PersonModel;
import org.czg.redis.center.biz.geo.service.NearbyService;
import org.czg.redis.center.result.ResultVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author czg
 */
@RestController
@RequestMapping("/geo")
@Slf4j
@RequiredArgsConstructor
public class NearbyController {

    private final NearbyService nearbyService;

    /**
     * 添加人员信息
     *
     * @param personModel 人员信息
     * @return 返回结果
     */
    @PostMapping("/person/add")
    public ResultVO<Void> addPerson(@Valid @RequestBody PersonModel personModel) {
        nearbyService.addPerson(personModel);
        return ResultVO.success();
    }

    /**
     * 获取附近的人
     *
     * @param nearbyModel 请求参数
     * @return 附近的人
     */
    @PostMapping("/person/nearby")
    public ResultVO<List<PersonModel>> getNearbyPersonList(@Valid @RequestBody NearbyModel nearbyModel) {
        return ResultVO.success(nearbyService.getNearByPersonList(nearbyModel));
    }

}

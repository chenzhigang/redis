package org.czg.redis.center.biz.geo.service;

import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.czg.redis.center.biz.geo.model.NearbyModel;
import org.czg.redis.center.biz.geo.model.PersonModel;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author czg
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NearbyService {

    private final GeoOperations<String, Object> geoOperations;

    private final static String PERSON_KEY_STR = "nearby:person";

    public void addPerson(PersonModel personModel) {
        geoOperations.add(PERSON_KEY_STR, new Point(personModel.getLongitude().doubleValue(), personModel.getLatitude().doubleValue()), JSON.toJSONString(personModel));
    }

    public List<PersonModel> getNearByPersonList(NearbyModel nearbyModel) {
        Point point = new Point(nearbyModel.getLongitude().doubleValue(), nearbyModel.getLatitude().doubleValue());
        Distance distance = new Distance(nearbyModel.getDistance().doubleValue(), Metrics.KILOMETERS);
        Circle circle = new Circle(point, distance);
        List<GeoResult<RedisGeoCommands.GeoLocation<Object>>> content = Objects.requireNonNull(geoOperations.radius(PERSON_KEY_STR, circle)).getContent();
        return content.stream().map(it -> JSON.parseObject(it.getContent().getName().toString(), PersonModel.class)).toList();
    }
}

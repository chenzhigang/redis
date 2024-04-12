package org.czg.redis.center.biz.rank.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author czg
 */
@Data
public class Rank implements Serializable {

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 用户得分
     */
    private Integer score;

}

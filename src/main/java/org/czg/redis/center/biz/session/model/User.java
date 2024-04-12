package org.czg.redis.center.biz.session.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author czg
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User implements Serializable {

    private Long userId;

    private String username;

    private String password;

    private String mobile;

}

package org.czg.redis.center.biz.list.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author czg
 */
@Data
public class LatestListModel {

    @NotBlank(message = "push内容不能为空")
    private String content;

}

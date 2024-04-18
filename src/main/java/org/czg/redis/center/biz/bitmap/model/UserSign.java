package org.czg.redis.center.biz.bitmap.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Data;

/**
 * @author czg
 */
@Data
public class UserSign {

    @NotNull(message = "用户id不能为空", groups = {Default.class, MakeUpSignInterface.class})
    private Long userId;

    @NotBlank(message = "签到（或补签）月份不能为空（根式：yyyyMM）", groups = {MakeUpSignInterface.class})
    private String dateTimeStr;

    @NotNull(message = "签到（或补签）日期不能为空", groups = {MakeUpSignInterface.class})
    private Integer dayOfMonth;

    public interface MakeUpSignInterface {

    }

}

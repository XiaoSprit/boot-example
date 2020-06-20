package org.xhxin.common.util;

import com.google.gson.annotations.Expose;
import lombok.Data;

import java.util.Date;
@Data
public class GsonBeans {
    private long id;
    private Date time;
    @Expose(serialize = false)
    private Date createTime;
    private Long a;
}

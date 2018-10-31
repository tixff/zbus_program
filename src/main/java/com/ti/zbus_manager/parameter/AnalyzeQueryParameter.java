package com.ti.zbus_manager.parameter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class AnalyzeQueryParameter {
    @ApiModelProperty("topic 名字")
    private String topicName;

    @ApiModelProperty("消息接收开始时间")
    private Date startTime;

    @ApiModelProperty("消息接收截止时间")
    private Date endTime;
}

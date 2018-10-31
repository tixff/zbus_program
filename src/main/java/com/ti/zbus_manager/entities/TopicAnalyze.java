package com.ti.zbus_manager.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TopicAnalyze {

    @ApiModelProperty("id")
    private int id;

    @ApiModelProperty("topic名字")
    private String topicName;

    @ApiModelProperty("连接时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date connectTime;

    @ApiModelProperty("连接数量")
    private int connectCount;

    @ApiModelProperty("消息接收时间")
    private Date receivedTime;

    @ApiModelProperty("消息内容")
    private String receivedDesc;

    @Override
    public String toString() {
        return "TopicAnalyze{" +
                "id=" + id +
                ", topicName='" + topicName + '\'' +
                ", connectTime=" + connectTime +
                ", connectCount=" + connectCount +
                '}';
    }
}

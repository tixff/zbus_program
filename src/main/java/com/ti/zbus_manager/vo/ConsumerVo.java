package com.ti.zbus_manager.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class ConsumerVo {
    @ApiModelProperty("接收时间")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date date;

    @ApiModelProperty("接收数量")
    private int count;

    @ApiModelProperty("topic名字")
    private String topicName;

    @ApiModelProperty("接收时间字符串格式")
    public String getDateStr() {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm").format(date);
    }

    @ApiModelProperty("接收数量字符串格式")
    public String getCountStr() {
        return String.valueOf(count);
    }

    public ConsumerVo(Date date, int count, String topicName) {
        this.date = date;
        this.count = count;
        this.topicName = topicName;
    }

    public ConsumerVo() {
    }

    public static void main(String[] args) {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));
    }
}

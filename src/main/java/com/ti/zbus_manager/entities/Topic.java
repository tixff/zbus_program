package com.ti.zbus_manager.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Topic {

    @ApiModelProperty("id")
    private int id;

    @ApiModelProperty("名字")
    private String name;

    @ApiModelProperty("添加时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("删除时间")
    private Date deleteTime;

    @ApiModelProperty("状态 0:正常，1:已删除")
    private int status;

    @ApiModelProperty("添加者的ip")
    private String ip;
}

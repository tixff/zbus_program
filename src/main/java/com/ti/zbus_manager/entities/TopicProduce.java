package com.ti.zbus_manager.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Ti
 * @date 2018/12/26
 */

@Data
public class TopicProduce {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("生产id")
    private String produceIp;

    @ApiModelProperty("生产时间")
    private Date produceTime;

    @ApiModelProperty("主题名称")
    private String topicName;

    public String getProduceTimeStr(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(produceTime);
    }
}

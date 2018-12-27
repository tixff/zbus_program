package com.ti.zbus_manager.mapper;

import com.ti.zbus_manager.entities.TopicProduce;
import com.ti.zbus_manager.parameter.AnalyzeQueryParameter;

import java.util.ArrayList;

public interface TopicProduceMapper {

    ArrayList<TopicProduce> findProduceByTopicName(String topicName);

    
    ArrayList<TopicProduce> findProduceByDateRange(AnalyzeQueryParameter parameter);

    void addTopicProduce(TopicProduce produce);
}

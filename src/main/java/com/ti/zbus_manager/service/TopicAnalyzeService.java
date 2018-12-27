package com.ti.zbus_manager.service;

import com.ti.zbus_manager.entities.TopicAnalyze;
import com.ti.zbus_manager.vo.ResultAnalyzeData;

import java.util.ArrayList;
import java.util.Date;

public interface TopicAnalyzeService {
    ArrayList<TopicAnalyze> getAllInfo();

    void addConsumer(String topicName);

    void produceMessage(String topicName,String message,String ip);

    ResultAnalyzeData getConsumerAnalyzeData(Date date,String topicName);

    ResultAnalyzeData getProduceAnalyzeData();
}

package com.ti.zbus_manager.service;

import com.ti.zbus_manager.entities.TopicAnalyze;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public interface TopicAnalyzeService {
    ArrayList<TopicAnalyze> getAllInfo();

    void addConsumer(String topicName);

    void produceMessage(String topicName,String message);

    HashMap<String,ArrayList> getAnalyzeData(Date date);
}

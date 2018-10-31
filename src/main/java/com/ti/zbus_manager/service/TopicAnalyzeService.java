package com.ti.zbus_manager.service;

import com.ti.zbus_manager.entities.TopicAnalyze;

import java.util.ArrayList;

public interface TopicAnalyzeService {
    ArrayList<TopicAnalyze> getAllInfo();

    void addCondumer(String topicName);

    void produceMessage(String topicName,String message);
}

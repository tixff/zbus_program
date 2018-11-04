package com.ti.zbus_manager.service;

import com.ti.zbus_manager.entities.Topic;

import java.util.ArrayList;

public interface TopicService {
    void addTopic(Topic topic);

    Topic findTopic(Topic topic);

    ArrayList<Topic> getAllTopic();
}

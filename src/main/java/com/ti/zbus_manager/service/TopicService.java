package com.ti.zbus_manager.service;

import com.ti.zbus_manager.entities.Topic;

public interface TopicService {
    void addTopic(Topic topic);

    Topic findTopic(Topic topic);
}

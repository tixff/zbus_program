package com.ti.zbus_manager.mapper;

import com.ti.zbus_manager.entities.Topic;

import java.util.ArrayList;

public interface TopicMapper {

    void addTopic(Topic t);

    void updateTopic(Topic t);

    void deleteTopic(Topic t);

    Topic findTopic(Topic topic);

    Topic findTopicByName(String topicName);

    ArrayList<Topic> getALlTopic();
}

package com.ti.zbus_manager.mapper;

import com.ti.zbus_manager.entities.Topic;
import org.springframework.cache.annotation.Cacheable;

import java.util.ArrayList;

public interface TopicMapper {

    void addTopic(Topic t);

    void updateTopic(Topic t);

    void deleteTopic(Topic t);

    Topic findTopic(Topic topic);

    Topic findTopicByName(String topicName);

    @Cacheable("topic")
    ArrayList<Topic> getALlTopic();
}

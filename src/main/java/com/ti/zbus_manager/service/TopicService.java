package com.ti.zbus_manager.service;

import com.ti.zbus_manager.entities.Topic;
import com.ti.zbus_manager.vo.ResultMessage;

import java.util.ArrayList;

public interface TopicService {
    ResultMessage addTopic(Topic topic);

    Topic findTopic(Topic topic);

    ArrayList<Topic> getAllTopic();
}

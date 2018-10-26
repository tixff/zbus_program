package com.ti.zbus_manager.service.impl;

import com.ti.zbus_manager.entities.Topic;
import com.ti.zbus_manager.mapper.TopicMapper;
import com.ti.zbus_manager.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicMapper mapper;

    @Override
    public void addTopic(Topic topic) {
        mapper.addTopic(topic);
    }

    @Override
    public Topic findTopic(Topic topic) {
        return mapper.findTopic(topic);
    }
}

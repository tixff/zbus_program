package com.ti.zbus_manager.service.impl;

import com.ti.zbus_manager.entities.Topic;
import com.ti.zbus_manager.mapper.TopicMapper;
import com.ti.zbus_manager.service.TopicService;
import io.zbus.mq.Broker;
import io.zbus.mq.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class TopicServiceImpl implements TopicService {
    private static Logger logger = LoggerFactory.getLogger(TopicServiceImpl.class);

    @Autowired
    private TopicMapper mapper;

    @Override
    public void addTopic(Topic topic) {
        topic.setCreateTime(new Date());
        try {
            mapper.addTopic(topic);
            Broker broker = new Broker("localhost:15555");
            Producer p = new Producer(broker);
            //创建topic
            p.declareTopic(topic.getName());
            broker.close();
        } catch (Exception e) {
            logger.error("添加topic出错");
        }
    }

    @Override
    public Topic findTopic(Topic topic) {
        return mapper.findTopic(topic);
    }

    @Override
    public ArrayList<Topic> getAllTopic() {
        ArrayList<Topic> aLlTopic = new ArrayList<>();
        try{
            aLlTopic = mapper.getALlTopic();
            return aLlTopic;
        }catch (Exception e){
            logger.error("获取所有主题信息失败");
            return aLlTopic;
        }
    }
}
